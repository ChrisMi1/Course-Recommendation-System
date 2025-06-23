package com.uniwa.course_recommendation.utils;

import com.uniwa.course_recommendation.entity.Course;
import com.uniwa.course_recommendation.service.OpenAIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ExplanationFetcher {

    private final OpenAIClient openAIClient;
    private final ExecutorService executor;

    public ExplanationFetcher(OpenAIClient client) {
        this.openAIClient = client;
        this.executor = Executors.newFixedThreadPool(4); // 4 threads
    }

    public void fetchExplanations(UserProfile userProfile, Set<Course> recommendedCourses) {
        List<Future<Void>> futures = new ArrayList<>();

        for (Course course : recommendedCourses) {
            String prompt = PromptBuilder.buildPrompt(userProfile, course);

            Callable<Void> task = () -> {
                try {
                    String explanation = openAIClient.getExplanation(prompt);
                    course.setExplanation(explanation);
                } catch (IOException ex) {
                    System.err.println("Error for course " + course.getName() + ": " + ex.getMessage());
                }
                return null;
            };

            futures.add(executor.submit(task));
        }


        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}

