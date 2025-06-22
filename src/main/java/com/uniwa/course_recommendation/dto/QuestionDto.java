package com.uniwa.course_recommendation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionDto {
   private Long id;
   private String question;
   private List<QuestionRulesDto> answers;
   private String type;
   @JsonIgnore
   private String options;


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof QuestionDto)) return false;
      QuestionDto question = (QuestionDto) o;
      return Objects.equals(id, question.id); // equality based on id
   }

   @Override
   public int hashCode() {
      return Objects.hash(id); // hashCode also based on id
   }
}
