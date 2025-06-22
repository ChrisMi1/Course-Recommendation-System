package com.uniwa.course_recommendation.dto;

import lombok.*;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResultsDto {
    private String flowSelected;
    private List<RecommendedCoursesDto> recommendedCoursesDtos;
}
