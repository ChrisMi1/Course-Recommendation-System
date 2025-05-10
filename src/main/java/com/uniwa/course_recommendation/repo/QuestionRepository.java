package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.dto.QuestionRulesDto;
import com.uniwa.course_recommendation.entity.DbEntity;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;

@Repository
public class QuestionRepository extends AbstractRepository<DbEntity> {

    public List<QuestionDto> getAllQuestions() {
        String query = """
                    SELECT * FROM questions;
                """;
        return executeNativeQuery(query,"QuestionMapping", new HashMap<>());
    }

    public List<QuestionRulesDto> getAllQuestionsWithAnswers() {
        String query2 = """
                SELECT question_id,answer_value,GROUP_CONCAT(next_question_id) AS next_question_id
                FROM questions q
                INNER JOIN question_rules qr ON  q.id = qr.question_id
                GROUP BY question_id,answer_value;
                """;
         return executeNativeQuery(query2,"QuestionRulesMapping",new HashMap<>());
    }
}
