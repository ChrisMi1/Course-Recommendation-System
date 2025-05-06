package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.entity.Question;
import com.uniwa.course_recommendation.repo.AbstractRepository;
import com.uniwa.course_recommendation.repo.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Autowired
    QuestionRepository questionRepository;

    public List<QuestionDto> findAllQuestions() {
        logger.info("Retrieving all questions with answers");
        return questionRepository.getAllQuestionsWithAnswers();
    }


}
