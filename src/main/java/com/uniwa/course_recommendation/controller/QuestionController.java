package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/first")
    public ResponseEntity<QuestionDto> firstQuestion() {
        return new ResponseEntity<>(questionService.getFirstQuestion(), HttpStatus.OK);
    }

    @GetMapping("/next")
    public ResponseEntity<List<QuestionDto>> nextQuestions(@RequestBody AnswerDto answerDto) {
        return new ResponseEntity<>(questionService.getNextQuestions(answerDto), HttpStatus.OK);
    }

}
