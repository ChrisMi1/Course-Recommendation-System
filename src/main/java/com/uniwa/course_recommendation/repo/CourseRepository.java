package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.BasicStreamCourses;
import com.uniwa.course_recommendation.entity.Course;
import com.uniwa.course_recommendation.entity.CourseChoices;
import com.uniwa.course_recommendation.entity.DbEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CourseRepository extends AbstractRepository<DbEntity> {
    public Course findCourseByName(String name) {
        String query = """
                SELECT c FROM Course c
                WHERE name = :nameCourse
                """;
        HashMap<String,Object> params = new HashMap<>();
        params.put("nameCourse",name);
        return (Course) jpqlQueryWithParamsSingleResult(query,params);
    }
    public List<CourseChoices> findCoreCourses(String specialization) {
        String query = """
                SELECT cc FROM CourseChoices cc
                WHERE answer1 = :specialization
                AND answer2 IS NULL
                """;
        HashMap<String,Object> params = new HashMap<>();
        params.put("specialization",specialization);
        return jpqlQueryWithParams(query,params,CourseChoices.class);
    }

    public CourseChoices findCoursesFromInterests(String answer1, String answer2) {
        String query = """
                SELECT cc FROM CourseChoices cc
                WHERE answer1 = :answer1
                AND answer2 = :answer2
                """;
        HashMap<String,Object> params = new HashMap<>();
        params.put("answer1",answer1);
        params.put("answer2",answer2);
        return (CourseChoices) jpqlQueryWithParamsSingleResult(query,params);
    }
    public List<BasicStreamCourses> findBasicStreamCourses(String flow) {
        String query = """
                SELECT sc FROM BasicStreamCourses sc
                WHERE title = :flow
                """;
        HashMap<String,Object> params = new HashMap<>();
        params.put("flow",flow);
        return jpqlQueryWithParams(query,params,BasicStreamCourses.class);
    }



}
