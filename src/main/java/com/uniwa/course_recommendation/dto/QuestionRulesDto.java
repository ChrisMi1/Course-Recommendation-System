package com.uniwa.course_recommendation.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRulesDto {
    private Long id;
    private String answer;
    private String nextQuestionId;
}
