package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizAnswers {

    private List<Integer> answers;

    public QuizAnswers() {
        answers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            answers.add(-1);
        }
    }
}
