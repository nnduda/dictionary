package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * quiz angielsko-polski, angielskie slowa i polskie tlumaczenia z xml'a jako odpowiedzi
 * quiz angielsko-angielski, dane bardziej z jsona
 */
@Data
public class Quiz {

    private List<String> words;
    private List<List<String>> answers;
    private List<Integer> correctAnswers; // numery poprawnych odpowiedzi, mogloby to tez byc List<String>
    private QuizType quizType;
    private QuizDataType quizDataType;

    public Quiz(QuizType quizType, QuizDataType quizDataType) {
        this.quizType = quizType;
        this.quizDataType = quizDataType;
        this.words = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
    }

    public void setWord(int index, String word) {
        this.words.set(index, word);
    }

}