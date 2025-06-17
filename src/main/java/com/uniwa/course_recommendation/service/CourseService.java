package com.uniwa.course_recommendation.service;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.RecommendedCoursesDto;
import com.uniwa.course_recommendation.entity.Course;
import com.uniwa.course_recommendation.exception.KeyNotFound;
import com.uniwa.course_recommendation.exception.RecommenderApiException;
import com.uniwa.course_recommendation.utils.JsonUtils;
import com.uniwa.course_recommendation.utils.UserProfileBuilder;
import okhttp3.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    RedisService redisService;
    @Autowired
    DesicionTreeService desicionTreeService;
    public List<RecommendedCoursesDto> findRecommendedCourses(String sessionId) {
        logger.info("Trying to find the recommended courses based on the answers of the user");
        List<AnswerDto> answers = redisService.getAnswers(sessionId);
        if (CollectionUtils.isEmpty(answers)) {
            logger.warn("No answers found for this session id");
            throw new KeyNotFound("Enable to find the key you provide");
        }
        String summary = UserProfileBuilder.buildProfileSummary(answers);
        logger.info("The generated summary is: " + summary);
        String coursesInJson = sendSummaryToRecommenderApi(summary);
        return retrieveCoursesFromJson(coursesInJson);
    }

    private List<RecommendedCoursesDto> retrieveCoursesFromJson(String coursesInJson) throws JsonSyntaxException {
        logger.info("Trying to retrieved courses from json");
        Type listType = new TypeToken<ArrayList<RecommendedCoursesDto>>(){}.getType();
        return JsonUtils.fromJson(coursesInJson, listType);
    }

    private String sendSummaryToRecommenderApi(String summary) {
        logger.info("Trying to send the summary in the recommended api");
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:8000/recommendations";
        RequestBody requestBody = RequestBody.create("{ \"summary\" : \"" + summary + "\" }", MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RecommenderApiException("Failed to find the recommended courses");
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RecommenderApiException("Failed to find the recommended courses as body is empty");
            }
            String recommendedCoursesInJson = responseBody.string();
            if (StringUtils.isEmpty(recommendedCoursesInJson)) {
                throw new RecommenderApiException("Failed to find the recommended courses as body is empty");
            }
            logger.info("Summary send with success and we received response from the api");
            return recommendedCoursesInJson;
        } catch (IOException ex) {
            throw new RecommenderApiException("Failed to find the recommended courses");
        }
    }


    public List<RecommendedCoursesDto> findRecommendedCourses2(String sessionId) {
        logger.info("Trying to find the recommended courses based on the answers of the user");
        List<AnswerDto> answers = redisService.getAnswers(sessionId);
        Set<Course> recommendedCourses = desicionTreeService.mapUserAnswersToCourses(answers);
        return Course.mapCourseToRecommendedCoursesDto(recommendedCourses);
    }
}
