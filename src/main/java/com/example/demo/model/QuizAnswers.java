package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizAnswers {

    private Long quizId;
    private List<Integer> answers;

    public QuizAnswers(Long quizId) {
        this.quizId = quizId;
        answers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            answers.add(-1);
        }
    }
}
