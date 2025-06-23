package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.service.OpenAIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ChatBotController {
    @Autowired
    OpenAIClient openAIClient;
    @GetMapping(value = "/chatbot",produces = "text/plain; charset=UTF-8")
    public ResponseEntity<String> chatBot(@RequestParam String messageUser) throws IOException, InterruptedException {
        String response = openAIClient.getResponseFromAssistant(messageUser);
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/plain;charset=UTF-8")).body(response);
    }
}
