package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.entity.DbEntity;
import com.uniwa.course_recommendation.entity.Question;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepository extends AbstractRepository<DbEntity> {

    public Question getFirstQuestion() {
        String query = "SELECT q FROM QuestionRules qr JOIN qr.question q WHERE qr.questionSequence = 0";
        return (Question) jpqlQuerySingleResult(query);
    }
}
