package com.uniwa.course_recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_rules")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRules extends AuditableDbEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "answer_value")
    private String answerValue;

    @Column(name = "next_question_id")
    private Long nextQuestion;

    @Column(name = "question_sequence")
    private Integer questionSequence;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Question Rules";
    }
}
