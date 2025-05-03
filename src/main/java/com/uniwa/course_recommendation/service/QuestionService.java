package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.FirstQuestionsDto;
import com.uniwa.course_recommendation.entity.Question;
import com.uniwa.course_recommendation.repo.AbstractRepository;
import com.uniwa.course_recommendation.repo.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Autowired
    QuestionRepository questionRepository;

    public FirstQuestionsDto getFirstQuestion() {
        logger.info("Retrieving first question...");
        Question question = questionRepository.getFirstQuestion();
        return question.updateFirstQuestionDto(question);
    }
    //getNextQuestion(String answer)
}
