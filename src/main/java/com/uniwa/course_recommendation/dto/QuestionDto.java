package com.uniwa.course_recommendation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
   private List<AnswerDto> answers;
   @JsonIgnore
   private String options;
}
