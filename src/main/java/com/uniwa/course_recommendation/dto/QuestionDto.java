package com.uniwa.course_recommendation.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionDto {
   private Long id;
   private String question;
   private List<String> options;
}
