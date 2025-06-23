package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.service.OpenAIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    @Autowired
    OpenAIClient openAIClient;
    public ResponseEntity<String> chatBot(String messageUser) throws IOException, InterruptedException {
        return new ResponseEntity<>(openAIClient.getResponseFromAssistant(messageUser), HttpStatus.OK);
    }
}
