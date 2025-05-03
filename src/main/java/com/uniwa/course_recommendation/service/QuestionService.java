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

    public QuestionDto getFirstQuestion() {
        logger.info("Retrieving first question...");
        Question question = questionRepository.getFirstQuestion();
        return Question.updateFirstQuestionDto(question);
    }

    public List<QuestionDto> getNextQuestions(AnswerDto answerDto) {
        logger.info("Retrieving next questions...");
        List<Question> questions = questionRepository.getNextQuestions(answerDto.getAnswer());
        List<QuestionDto> questionsDto = new ArrayList<>();
        questions.forEach(question -> questionsDto.add(Question.updateFirstQuestionDto(question)));
        return questionsDto;
    }


}
