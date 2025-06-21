package com.uniwa.course_recommendation.entity;

import com.uniwa.course_recommendation.dto.QuestionDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SqlResultSetMapping(name = "QuestionMapping",
        classes = @ConstructorResult(
                targetClass = QuestionDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "question", type = String.class),
                        @ColumnResult(name = "options", type = String.class),
                        @ColumnResult(name = "type", type = String.class)
                }))
public class Question extends AuditableDbEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "options")
    private String options;

    @Column(name = "type")
    private String type;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Question";
    }


}
