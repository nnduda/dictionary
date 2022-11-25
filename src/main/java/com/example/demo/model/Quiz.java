package com.example.demo.model;

import java.util.List;

/**
 * quiz angielsko-polski, angielskie slowa i polskie tlumaczenia z xml'a jako odpowiedzi
 * quiz angielsko-angielski, dane bardziej z jsona
 */
public class Quiz {

    List<String> words;
    List<List<String>> answers;
    List<Integer> correctAnswers; // numery poprawnych odpowiedzi, mogloby to tez byc List<String>
    // TODO stworzyc enumy z ponizszych:
    QuizType quizType; // TRANSLATIONS,MEANINGS
    QuizDataType quizDataType; // RANDOM, PRESELECT, PRESEARCHED

    /*
    TODO (na zajecia) serwis do tworzenia quizow
    moze repozytorium, na quizy, na linki

    docelowo UI do potestowania
     */

}
