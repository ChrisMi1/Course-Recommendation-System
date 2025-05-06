package com.uniwa.course_recommendation.repo;

import com.uniwa.course_recommendation.dto.AnswerDto;
import com.uniwa.course_recommendation.dto.QuestionDto;
import com.uniwa.course_recommendation.entity.DbEntity;
import com.uniwa.course_recommendation.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class QuestionRepository extends AbstractRepository<DbEntity> {

    public List<QuestionDto> getAllQuestions() {
        String query = """
                    SELECT * FROM questions;
                """;
        return executeNativeQuery(query,"QuestionMapping", new HashMap<>());
    }

    public List<QuestionDto> getAllQuestionsWithAnswers() {

        String query2 = """
                SELECT question_id,answer_value,GROUP_CONCAT(next_question_id) AS next_question_id
                FROM questions q
                INNER JOIN question_rules qr ON  q.id = qr.question_id
                GROUP BY question_id,answer_value;
                """;
        List<QuestionDto> questions = getAllQuestions();
        List<AnswerDto> answers = executeNativeQuery(query2,"QuestionRulesMapping",new HashMap<>());
        questions.forEach(questionDto ->
        {
            questionDto.setAnswers(answers.stream().filter(answerDto -> Objects.equals(answerDto.getId(), questionDto.getId()))
                    .collect(Collectors.toList()));
            if (questionDto.getAnswers().isEmpty()) {
                List<String> options = new ArrayList<>(List.of(questionDto.getOptions().split(",")));
                List<AnswerDto> answersDto = new ArrayList<>();
                options.forEach(option ->
                        {
                            answersDto.add(AnswerDto.builder()
                                    .id(questionDto.getId())
                                    .answer(option)
                                    .build());

                        }
                );
                questionDto.setAnswers(answersDto);
            }

        }



        );
        return questions;
    }
}
