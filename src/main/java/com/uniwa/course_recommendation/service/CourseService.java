package com.uniwa.course_recommendation.service;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.RecommendedCoursesDto;
import com.uniwa.course_recommendation.entity.BasicStreamCourses;
import com.uniwa.course_recommendation.entity.Course;
import com.uniwa.course_recommendation.exception.KeyNotFound;
import com.uniwa.course_recommendation.exception.RecommenderApiException;
import com.uniwa.course_recommendation.repo.CourseRepository;
import com.uniwa.course_recommendation.utils.JsonUtils;
import com.uniwa.course_recommendation.utils.UserProfileBuilder;
import okhttp3.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    DecisionTreeService decisionTreeService;
    @Autowired
    CourseRepository courseRepository;

    public List<RecommendedCoursesDto> findRecommendedCourses(String sessionId) {
        logger.info("Trying to find the recommended courses based on the answers of the user");
        List<AnswerDto> answers = redisService.getAnswers(sessionId);
        if (CollectionUtils.isEmpty(answers)) {
            logger.warn("No answers found for this session id");
            throw new KeyNotFound("Enable to find the key you provide");
        }
        Set<Course> recommendedCourses = decisionTreeService.mapUserAnswersToCourses(answers);
        List<RecommendedCoursesDto> recommendedCoursesDtos = Course.mapCourseToRecommendedCoursesDto(recommendedCourses);
        findPrerequestCourses(answers,recommendedCoursesDtos);
        findMandatoryCoursesForUserStream(answers.get(0).getAnswer(),recommendedCoursesDtos);
        return recommendedCoursesDtos;
    }

    @Transactional
    private void findMandatoryCoursesForUserStream(String flow,List<RecommendedCoursesDto> recommendedCoursesDtos) {
        //TODO:CHECK IF NOT RETURNS NOTHING AND RETURN EXCEPTION
        List<BasicStreamCourses> basicStreamCourses =  courseRepository.findBasicStreamCourses(flow);
        basicStreamCourses.forEach(course -> recommendedCoursesDtos.add(RecommendedCoursesDto.builder()
                        .id(course.getCourse().getId())
                        .name(course.getCourse().getName())
                        .flow(flow)
                        .isMandatory(true)
                        .isPrerequest(false)
                        .url(course.getCourse().getUrl())
                        .build()));
    }

    private void findPrerequestCourses(List<AnswerDto> answers,List<RecommendedCoursesDto> recommendedCoursesDtos) {
        String summary = UserProfileBuilder.buildProfileSummary(answers);
        String coursesInJson = sendSummaryToRecommenderApi(summary);
        recommendedCoursesDtos.addAll(retrieveCoursesFromJson(coursesInJson));
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
            logger.info("Summary send with success and we received response from the api this is the answer: " + recommendedCoursesInJson);
            return recommendedCoursesInJson;
        } catch (IOException ex) {
            throw new RecommenderApiException("Failed to find the recommended courses");
        }
    }
}
