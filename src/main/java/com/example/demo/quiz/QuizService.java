package com.example.demo.quiz;

import com.example.demo.exceptions.AnswerNotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Type;
import com.example.demo.model.xml.Word;
import com.example.demo.words.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    private WordsService wordsService;

    @Autowired
    public QuizService(WordsService wordsService) {
        this.wordsService = wordsService;
    }

    public Quiz createRandomQuiz() {
        Quiz quiz = new Quiz(QuizType.TRANSLATIONS, QuizDataType.RANDOM);
        List<Word> randomWords = getRandomWords(10);
        //fillQuizQuestions(quiz, randomWords);
        // TODO wordsIds moze byc wyliczane w prepareAnswers
        List<Long> wordsIds = randomWords.stream()
                .map(Word::getId)
                .toList();
        try {
            prepareAnswers(quiz, randomWords, wordsIds);
        } catch (AnswerNotFoundException e) {
            System.out.println("Nie znaleziono odpowiedzi dla slowa o id " + e.getId());
            e.printStackTrace();
        }
        return quiz;
    }

    private void fillQuizQuestions(Quiz quiz, List<Word> randomWords) {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (Word word : randomWords) {
            quizQuestions.add(new QuizQuestion(word.getWord()));
        }
        quiz.setQuizQuestions(quizQuestions);
    }

    private List<Word> getRandomWords(int wordsCount) {
        Random random = new Random();
        long numWords = wordsService.countWords();
        List<Word> words = new ArrayList<>();
        while (words.size() < wordsCount) {
            Optional<Word> word = wordsService.getWordById(random.nextLong() % numWords);
            word.ifPresent(words::add);
        }
        return words;
    }

    private List<Long> setWordsAndGetWordsIds(Quiz quiz) {
        List<String> words = new ArrayList<>();
        List<Long> wordsIds = new ArrayList<>();
        Random random = new Random();
        long numWords = wordsService.countWords();
        while (words.size() < 10) {
            Optional<Word> word = wordsService.getWordById(random.nextLong() % numWords);
            if (word.isPresent()) {
                words.add(word.get().getWord());
                wordsIds.add(word.get().getId());
            }
        }

        return wordsIds;
    }

    // TODO nazwa delikatnie do zmiany jesli tworzymy tutaj QuizQuestions
    private void prepareAnswers(Quiz quiz, List<Word> randomWords, List<Long> wordsIds) throws AnswerNotFoundException { // wordsIds - identyfikatory slow: "dog","cat","turtle","lion","fish"
        List<String> correctAnswers = new ArrayList<>(); // poprawne odpowiedzi dla kolejnych slow np. lista: pies, kot, zolw, lew, ryba


        // wyszukiwanie tlumaczen/znaczen
        for (int i = 0; i < wordsIds.size(); i++) {
            if (QuizType.TRANSLATIONS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(wordsIds.get(i), i, quiz));
            } else if (QuizType.MEANINGS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(quiz.getQuizQuestions().get(i).getWord())); // TODO do zmiany analogicznie jak translations?
            }
        }

        Random r = new Random();
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (int i = 0; i < wordsIds.size(); i++) {
            Set<Integer> chosenAnswersSet = new LinkedHashSet<>();
            // aktualnie wybrane/wylosowane odpowiedzi (juz uzyte)
            chosenAnswersSet.add(i); // dodanie numeru poprawnej odpowiedzi
            // losowanie 3 pozostalych odpowiedzi:
            while (chosenAnswersSet.size() != 4) {
                chosenAnswersSet.add(r.nextInt(wordsIds.size()));
            }
            List<Integer> chosenAnswers = new ArrayList<>(chosenAnswersSet);
            // 3,4,1,2
            int correctAnswerNumber = r.nextInt(4); // np. 2, numer poprawnej odpowiedzi (z listy answers)

            Integer correctAnswer = chosenAnswers.remove(0);//  4,1,2
            chosenAnswers.add(correctAnswerNumber, correctAnswer); // 4,1,3,2


            List<String> answers = new ArrayList<>(); // wylosowane odpowiedzi do danego slowa
            for (Integer answerNumber : chosenAnswers) {
                answers.add(correctAnswers.get(answerNumber));
            }

            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setWord(randomWords.get(i).getWord());
            quizQuestion.setAnswers(answers);
            quizQuestion.setCorrectAnswer(correctAnswerNumber);
            quizQuestions.add(quizQuestion);
            //quiz.getQuizQuestions().get(i).setAnswers(answers);
            //quiz.getQuizQuestions().get(i).setCorrectAnswer(correctAnswerNumber);
        }
        quiz.setQuizQuestions(quizQuestions);
    }

    // TODO

    /**
     * Pobranie odpowiedzi dla slowa o podanym wordId z zewnetrznego API.
     *
     * @param word slowo do wyszukania w API.
     * @return znaleziona odpowiedz.
     */
    private String getAnswer(String word) {
        return null;
    }

    /**
     * Pobranie odpowiedzi dla slowa o podanym wordId z bazy danych.
     *
     * @param wordId identyfikator slowa.
     * @return znaleziona odpowiedz.
     */
    private String getAnswer(Long wordId, int wordIdx, Quiz quiz) throws AnswerNotFoundException {
        Random random = new Random();
        Optional<Word> wordOptional = wordsService.getWordById(wordId);
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            List<Translation> translations = word.getTranslations();
            Translation translation = translations.get(random.nextInt(translations.size())); // TODO do sprawdzenia translations.size() moze byc rowne 0?
            String quote = translation.getQuote();
            if (Type.IDIOM.equals(translation.getType())) {
                List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
                QuizQuestion quizQuestion = quizQuestions.get(wordIdx);
                quizQuestion.setWord(translation.getPhrase());
            }

            return quote;
        }
        throw new AnswerNotFoundException(wordId);
    }

    public List<Boolean> calculateResults(Quiz quiz, QuizAnswers quizAnswers) {
        List<Integer> correctAnswers = quiz.getQuizQuestions().stream().map(question -> question.getCorrectAnswer()).toList();
        List<Integer> answersFromUser = quizAnswers.getAnswers();
        List<Boolean> results = new ArrayList<>();
        for (int i = 0; i < correctAnswers.size(); i++) {
            results.add(correctAnswers.get(i).equals(answersFromUser.get(i))); // answers.add(correctAnswers.get(i).equals(answersFromUser.get(i)) ? true : false);
        }

        return results;

    }
}