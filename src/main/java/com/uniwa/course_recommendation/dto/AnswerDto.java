package com.uniwa.course_recommendation.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerDto {
    private Long id;
    private String answer;
}
