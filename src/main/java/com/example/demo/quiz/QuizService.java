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
        try {
            prepareQuizQuestions(quiz, randomWords);
        } catch (AnswerNotFoundException e) {
            System.out.println("Nie znaleziono odpowiedzi dla slowa o id " + e.getId());
            e.printStackTrace();
        }
        return quiz;
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

    private void prepareQuizQuestions(Quiz quiz, List<Word> randomWords) throws AnswerNotFoundException { // wordsIds - identyfikatory slow: "dog","cat","turtle","lion","fish"
        List<String> correctAnswers = new ArrayList<>(); // poprawne odpowiedzi dla kolejnych slow np. lista: pies, kot, zolw, lew, ryba

        List<Long> wordsIds = randomWords.stream()
                .map(Word::getId)
                .toList();

        // wyszukiwanie tlumaczen/znaczen
        for (int i = 0; i < wordsIds.size(); i++) {
            if (QuizType.TRANSLATIONS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(wordsIds.get(i), i, quiz, randomWords));
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
     * @param wordId  identyfikator slowa w bazie danych
     * @param wordIdx indeks z petli (numer slowa w quizie)
     * @param quiz    quiz
     * @return znaleziona odpowiedz.
     */
    // TODO do zastanowienia (priorytetowo)!
    private String getAnswer(Long wordId, int wordIdx, Quiz quiz, List<Word> randomWords) throws AnswerNotFoundException {
        Random random = new Random();
        Optional<Word> wordOptional = wordsService.getWordById(wordId);
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            List<Translation> translations = word.getTranslations();
            Translation translation = translations.get(random.nextInt(translations.size())); // TODO do sprawdzenia translations.size() moze byc rowne 0?
            String quote = translation.getQuote();
            if (Type.IDIOM.equals(translation.getType())) {
                randomWords.get(wordIdx).setWord(translation.getPhrase());
                return translation.getQuote();
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