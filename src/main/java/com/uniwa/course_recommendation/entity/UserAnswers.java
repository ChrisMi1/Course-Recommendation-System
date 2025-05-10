package com.uniwa.course_recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAnswers extends AuditableDbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "session_id")
    private String sessionId;
    @Column(name = "track_id")
    private String trackId;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(name = "answer")
    private String answer;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "User answers";
    }
}
