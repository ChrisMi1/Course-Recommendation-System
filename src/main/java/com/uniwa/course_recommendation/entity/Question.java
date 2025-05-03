package com.uniwa.course_recommendation.entity;

import com.uniwa.course_recommendation.dto.FirstQuestionsDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question extends AuditableDbEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "options")
    private String options;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Question";
    }

    public FirstQuestionsDto updateFirstQuestionDto(Question question) {

        return FirstQuestionsDto.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .options(new ArrayList<>(List.of(question.getOptions().split(","))))
                .build();
    }
}
