package com.uniwa.course_recommendation.utils;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfile {
    private String flow;
    private String flowLabels;
    private String specialization;
    private String specializationLabels;
    private String interests;
    private String interestsLabels;
}
