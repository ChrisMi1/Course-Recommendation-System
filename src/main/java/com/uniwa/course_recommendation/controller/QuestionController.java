package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> findAllQuestions(HttpSession httpSession) {
        return new ResponseEntity<>(questionService.findAllQuestionsWithQuestionRules(),HttpStatus.OK);
    }

    @PostMapping("/answers")
    public ResponseEntity<String> submitAnswers(@RequestBody List<AnswerDto> answerDtoList,HttpSession httpSession) {
        questionService.saveAnswers(answerDtoList,httpSession.getId(),httpSession);
        return new ResponseEntity<>("Successfully saved the user answers",HttpStatus.OK);
    }
}
