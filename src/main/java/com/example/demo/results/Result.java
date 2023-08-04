package com.example.demo.results;

import lombok.Getter;

import java.util.List;

@Getter
public class Result {

    private final List<String> questions;
    private final List<String> userAnswers;
    private final List<String> correctAnswers;
    private final double result;

    public Result(List<String> questions, List<String> userAnswers, List<String> correctAnswers, double counter) {
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.correctAnswers = correctAnswers;
        this.result = counter / questions.size();
    }
}
