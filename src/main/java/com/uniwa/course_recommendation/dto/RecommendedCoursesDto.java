package com.uniwa.course_recommendation.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecommendedCoursesDto {
    private Long id;
    private String name;
    private Double similarity;
}
