package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.dto.FirstQuestionsDto;
import com.uniwa.course_recommendation.entity.Question;
import com.uniwa.course_recommendation.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/first")
    public ResponseEntity<FirstQuestionsDto> firstQuestion() {
        return new ResponseEntity<>(questionService.getFirstQuestion(), HttpStatus.OK);
    }
}
