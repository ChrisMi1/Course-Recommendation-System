package com.uniwa.course_recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Service
public class OpenAIClient {
    @Value("${openai.api.key}")
    private String apiKey;
    Logger logger = LoggerFactory.getLogger(CourseService.class);

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String ASSISTANT_URL = "https://api.openai.com/v1";
    private static final String ASSISTANT_ID = "asst_sClTBS7MJTxKOVhyi3O07jqi";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String systemMessage = """
            Είσαι ένας σύμβουλος σπουδών που εξηγεί σε φοιτητές γιατί τους προτάθηκε ένα συγκεκριμένο μάθημα.
            ΜΗΝ χρησιμοποιείς markdown ή έντονα γράμματα. Χρησιμοποίησε απλή λίστα bullets (π.χ. - ) με απλό κείμενο.
            ΜΗΝ αναφέρεις ονόματα ετικετών (π.χ. Flow Software, AI & ML, Software Engineering).
            Εστίασε στο περιεχόμενο του μαθήματος και πώς σχετίζεται με τα ενδιαφέροντα, την κατεύθυνση και τη ροή του φοιτητή.  
            """;

    public String getExplanation(String userPrompt) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "temperature", 0.7,
                "max_tokens", 500,
                "messages", List.of(
                        Map.of("role", "system", "content", systemMessage),
                        Map.of("role", "user", "content", userPrompt)
                )
        );
        String json = objectMapper.writeValueAsString(requestBody);
        Request request = new Request.Builder()
                .url(OPENAI_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json; charset=utf-8")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            int code = response.code();
            String msg = response.message();
            if (!response.isSuccessful()) {
                logger.error("OpenAI returned error status {} {}", code, msg);
                throw new IOException("Unexpected response: " + code + " " + msg);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Not found the description as body is empty");
            }

            String responseString = responseBody.string();
            logger.info(responseString);
            Map<String, Object> result = objectMapper.readValue(responseString, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }

            throw new IOException("No choices returned from OpenAI.");

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
    public String getResponseFromAssistant(String userPrompt) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        Request threadRequest = new Request.Builder()
                .url(ASSISTANT_URL + "/threads")
                .post(RequestBody.create("{}", MediaType.get("application/json; charset=utf-8")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("OpenAI-Beta", "assistants=v2")
                .addHeader("Content-Type", "application/json")
                .build();

        String threadId;
        try (Response response = client.newCall(threadRequest).execute()) {
            Map<String, Object> json = objectMapper.readValue(response.body().string(), Map.class);
            threadId = (String) json.get("id");
        }

        Map<String, Object> messageBody = Map.of(
                "role", "user",
                "content", userPrompt
        );
        String messageJson = objectMapper.writeValueAsString(messageBody);

        Request messageRequest = new Request.Builder()
                .url(ASSISTANT_URL + "/threads/" + threadId + "/messages")
                .post(RequestBody.create(messageJson, MediaType.get("application/json; charset=utf-8")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("OpenAI-Beta", "assistants=v2")
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(messageRequest).execute().close();

        Map<String, Object> runBody = Map.of(
                "assistant_id", ASSISTANT_ID
        );
        String runJson = objectMapper.writeValueAsString(runBody);

        Request runRequest = new Request.Builder()
                .url(ASSISTANT_URL + "/threads/" + threadId + "/runs")
                .post(RequestBody.create(runJson, MediaType.get("application/json; charset=utf-8")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("OpenAI-Beta", "assistants=v2")
                .addHeader("Content-Type", "application/json")
                .build();

        String runId;
        try (Response response = client.newCall(runRequest).execute()) {
            Map<String, Object> runResp = objectMapper.readValue(response.body().string(), Map.class);
            runId = (String) runResp.get("id");
        }

        String status;
        do {
            Thread.sleep(1000);
            Request statusRequest = new Request.Builder()
                    .url(ASSISTANT_URL + "/threads/" + threadId + "/runs/" + runId)
                    .get()
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("OpenAI-Beta", "assistants=v2")
                    .build();

            try (Response response = client.newCall(statusRequest).execute()) {
                Map<String, Object> statusResp = objectMapper.readValue(response.body().string(), Map.class);
                status = (String) statusResp.get("status");
            }
        } while (!status.equals("completed"));

        Request messagesRequest = new Request.Builder()
                .url(ASSISTANT_URL + "/threads/" + threadId + "/messages")
                .get()
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build();

        try (Response response = client.newCall(messagesRequest).execute()) {
            Map<String, Object> messagesResp = objectMapper.readValue(response.body().string(), Map.class);
            List<Map<String, Object>> messages = (List<Map<String, Object>>) messagesResp.get("data");
            if (!messages.isEmpty()) {
                Map<String, Object> lastMessage = messages.get(0);
                Map<String, Object> contentBlock = ((List<Map<String, Object>>) lastMessage.get("content")).get(0);
                return (String) contentBlock.get("text");
            }
        }

        return "Κάτι πήγε λάθος.";
    }
}



