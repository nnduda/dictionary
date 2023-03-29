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

    /*
    Rozwiazanie 1:
    Quiz:
        id, words (lista rozdzielona przecinkiem), answers (lista rozdz. przec. i dod. znakiem), correctAnswers (list. rozdz. przec.), quizType, quizDataType
     */
    /*
    Rozwiazanie 2: <- wybrane
    Analogicznie jak Word i Translation
    Quiz:
        id, quizType, quizDataType
    QuizQuestion:
        id, quiz_id, word, answers (lista rozdzielona srednikiem), correct answer
     */
    // TODO words, answers, correctAnswers do przeniesienia do QuizQuestion
    private List<String> words;
    private List<List<String>> answers; // 10 elementow po 4 tlumaczenia kazdy
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


