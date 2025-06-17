package com.uniwa.course_recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer_labels")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerLabels extends DbEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "labels")
    private String labels;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Answer Labels";
    }
}
