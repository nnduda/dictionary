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
    private List<String> answers; // 4 tlumaczenia
    private int correctAnswer; // numer poprawnej odpowiedzi

    public QuizQuestion() {
    }

    public QuizQuestion(String word) {
        this.word = word;
    }

}
