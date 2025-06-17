package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.AnswerLabels;
import com.uniwa.course_recommendation.entity.DbEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AnswerLabelRepository  extends AbstractRepository<DbEntity>{
    public List<AnswerLabels> getAnswerLabelsByAnswers(List<String> answers) {
        StringBuilder params = new StringBuilder();
        for (int i = 0; i<answers.size(); i++) {
            params.append(i != answers.size() - 1 ? "'" + answers.get(i) + "'," : "'" + answers.get(i) + "'");
        }
        String query = """
                SELECT * FROM answer_labels
                WHERE answer IN (%s);
                """.formatted(params);
        return nativeQuery(query,AnswerLabels.class);
    }
}
