package com.uniwa.course_recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_choices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseChoices extends DbEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "answer_1")
    private String answer1;

    @Column(name = "answer_2")
    private String answer2;

    @Column(name = "courses")
    private String courses;

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Course Choices";
    }
}
