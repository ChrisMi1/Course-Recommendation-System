package com.uniwa.course_recommendation.controller;

import com.uniwa.course_recommendation.dto.RecommendedCoursesDto;
import com.uniwa.course_recommendation.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    CourseService courseService;
    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendedCoursesDto>> showRecommendedCourses(HttpSession httpSession) {
        return new ResponseEntity<>(courseService.findRecommendedCourses(httpSession.getId()), HttpStatus.OK);
    }
}
