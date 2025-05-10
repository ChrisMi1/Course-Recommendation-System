package com.uniwa.course_recommendation.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerDto  {
    private Long questionId;
    private String question;
    private String answer;
}
