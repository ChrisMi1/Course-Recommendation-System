package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.dto.QuestionRulesDto;
import com.uniwa.course_recommendation.entity.Question;
import com.uniwa.course_recommendation.entity.UserAnswers;
import com.uniwa.course_recommendation.repo.AbstractRepository;
import com.uniwa.course_recommendation.repo.QuestionRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    RedisService redisService;

    public List<QuestionDto> findAllQuestionsWithQuestionRules() {
        logger.info("Retrieving all questions with answers");
        List<QuestionRulesDto> questionRules = questionRepository.getAllQuestionsWithAnswers();
        List<QuestionDto> questions = questionRepository.getAllQuestions();
        questions.forEach(questionDto ->
        {
            questionDto.setAnswers(questionRules.stream().filter(ruleDto -> Objects.equals(ruleDto.getId(), questionDto.getId()))
                    .collect(Collectors.toList()));
            if (questionDto.getAnswers().isEmpty()) {
                List<String> options = new ArrayList<>(List.of(questionDto.getOptions().split("\\|")));
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
        logger.info("Retrieved with success");
        return questions;
    }
    @Transactional
    public void saveAnswers(List<AnswerDto> answerDtoList, String sessionId, HttpSession httpSession) {
        String trackId = UUID.randomUUID().toString();
        logger.info("trying to save user answers...");
        answerDtoList.forEach(answer ->
                {
                    Question question = questionRepository.getReferenceById(Question.class,answer.getQuestionId());
                    UserAnswers userAnswers = UserAnswers.builder()
                            .sessionId(sessionId)
                            .trackId(trackId)
                            .question(question)
                            .answer(answer.getAnswer())
                            .build();
                    questionRepository.add(userAnswers);
                }
        );
        redisService.saveAnswers(sessionId,answerDtoList);
        logger.info("user answers saved successfully");
    }
}
