package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.DbEntity;
import com.uniwa.course_recommendation.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository extends AbstractRepository<DbEntity> {

    public Question getFirstQuestion() {
        String query = "SELECT q FROM QuestionRules qr JOIN qr.question q WHERE qr.questionSequence = 0";
        return (Question) jpqlQuerySingleResult(query);
    }

    public List<Question> getNextQuestions(String answer) {
        String query = """
                SELECT q.* FROM questions q 
                INNER JOIN question_rules qr ON q.id = qr.next_question_id 
                WHERE qr.answer_value = '%s' 
                ORDER BY question_sequence
                """.formatted(answer);
        return nativeQuery(query,Question.class);
    }
}
