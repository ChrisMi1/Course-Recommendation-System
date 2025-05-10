package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.dto.QuestionRulesDto;
import com.uniwa.course_recommendation.repo.AbstractRepository;
import com.uniwa.course_recommendation.repo.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Autowired
    QuestionRepository questionRepository;

    public List<QuestionDto> findAllQuestionsWithQuestionRules() {
        logger.info("Retrieving all questions with answers");
        List<QuestionRulesDto> questionRules = questionRepository.getAllQuestionsWithAnswers();
        List<QuestionDto> questions = questionRepository.getAllQuestions();
        questions.forEach(questionDto ->
        {
            questionDto.setAnswers(questionRules.stream().filter(ruleDto -> Objects.equals(ruleDto.getId(), questionDto.getId()))
                    .collect(Collectors.toList()));
            if (questionDto.getAnswers().isEmpty()) {
                List<String> options = new ArrayList<>(List.of(questionDto.getOptions().split(",")));
                List<QuestionRulesDto> answersDto = new ArrayList<>();
                options.forEach(option ->
                        {
                            answersDto.add(QuestionRulesDto.builder()
                                    .id(questionDto.getId())
                                    .answer(option)
                                    .build());

                        }
                );
                questionDto.setAnswers(answersDto);
            }

        });
        return questions;
    }


}
