package com.uniwa.course_recommendation.entity;

import com.uniwa.course_recommendation.dto.RecommendedCoursesDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course extends AuditableDbEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "embedding")
    private String embedding;

    @Column(name = "url")
    private String url;

    @Column(name = "labels")
    private String labels;

    @Column(name = "short_desc")
    private String shortDesc;

    @Transient
    private String explanation;
    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getEntityTitle() {
        return "Course";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course other)) return false;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static List<RecommendedCoursesDto> mapCourseToRecommendedCoursesDto(Set<Course> courses) {
        System.out.println("Courses set: " + courses);
        List<RecommendedCoursesDto> recommendedCoursesDtos = new ArrayList<>();
        courses.forEach(cou -> {
            if (cou.getLabels().contains("Flow Software")){
                recommendedCoursesDtos.add(RecommendedCoursesDto.
                        builder()
                        .id(cou.getId())
                        .flow("Ροή Λογισμικού και Πληροφοριακών Συστημάτων")
                        .name(cou.getName())
                        .url(cou.getUrl())
                        .explanation(cou.getExplanation())
                        .build());

            } else if (cou.getLabels().contains("Flow Network")) {
                recommendedCoursesDtos.add(RecommendedCoursesDto.
                        builder()
                        .id(cou.getId())
                        .flow("Ροή Δικτύων Υπολογιστών και Επικοινωνιών")
                        .name(cou.getName())
                        .url(cou.getUrl())
                        .explanation(cou.getExplanation())
                        .build());

            } else {
                recommendedCoursesDtos.add(RecommendedCoursesDto.
                        builder()
                        .id(cou.getId())
                        .flow("Ροή Υλικού και Υπολογιστικών Συστημάτων")
                        .name(cou.getName())
                        .url(cou.getUrl())
                        .explanation(cou.getExplanation())
                        .build());
            }

        });
        return recommendedCoursesDtos;
    }
}
