package com.example.demo.quiz;

import com.example.demo.model.Quiz;
import com.example.demo.model.QuizDataType;
import com.example.demo.model.QuizType;
import com.example.demo.model.xml.Word;
import com.example.demo.words.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuizService {

    private WordsService wordsService;

    @Autowired
    public QuizService(WordsService wordsService) {
        this.wordsService = wordsService;
    }

    public Quiz createRandomQuiz() {
        Quiz quiz = new Quiz(QuizType.TRANSLATIONS, QuizDataType.RANDOM);
        List<Word> words = new ArrayList<>();
        Random random = new Random();

        // TODO while zamiast for

        for (int i = 0; i < 10; ) {
            Optional<Word> word = wordsService.getWordById(random.nextLong());
            if (word.isPresent()) {
                words.add(word.get());
                i++;
            }
        }

        // TODO dodac wylosowane slowa do quizu
        return quiz;
    }
}
