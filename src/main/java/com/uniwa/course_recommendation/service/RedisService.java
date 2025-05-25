package com.uniwa.course_recommendation.service;

import com.uniwa.course_recommendation.dto.AnswerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisService {
    Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public void saveAnswers(String key, List<AnswerDto> answers) {
        redisTemplate.delete(key);
        answers.forEach(answer -> redisTemplate.opsForList().rightPush(key, answer));

    }
    public List<AnswerDto> getAnswers(String key) {
        logger.info("Retrieving answers of the user from redis...");
        return redisTemplate.opsForList().range(key, 0, -1).stream().map(object -> (AnswerDto) object).collect(Collectors.toList());
    }
}
