package com.uniwa.course_recommendation.dto;

import com.google.gson.annotations.SerializedName;
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
    private String flow;
    private boolean isMandatory;
    @SerializedName("prerequest")
    private boolean isPrerequest;
    private String url;
}
