package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> findAllQuestions() {
        return new ResponseEntity<>(questionService.findAllQuestions(),HttpStatus.OK);
    }


}
