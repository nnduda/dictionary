package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * quiz angielsko-polski, angielskie slowa i polskie tlumaczenia z xml'a jako odpowiedzi
 * quiz angielsko-angielski, dane bardziej z jsona
 */
@Data
@Entity(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private QuizType quizType;
    private QuizDataType quizDataType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id") // kolumna z tablicy quiz
    private List<QuizQuestion> quizQuestions;

    public Quiz(QuizType quizType, QuizDataType quizDataType) {
        this.quizType = quizType;
        this.quizDataType = quizDataType;
        this.quizQuestions = new ArrayList<>();
    }

}


