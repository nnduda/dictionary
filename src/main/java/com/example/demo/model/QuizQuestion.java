package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "quizquestions")
@Data
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;
    @Convert(converter = StringListConverter.class)
    private List<String> answers; // 10 elementow po 4 tlumaczenia kazdy
    private int correctAnswer; // numery poprawnych odpowiedzi, mogloby to tez byc List<String>

    public QuizQuestion() {
    }

    public QuizQuestion(String word) {
        this.word = word;
    }

}
