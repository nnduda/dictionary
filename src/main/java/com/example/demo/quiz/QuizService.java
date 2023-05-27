package com.example.demo.quiz;

import com.example.demo.exceptions.AnswerNotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Type;
import com.example.demo.model.xml.Word;
import com.example.demo.words.SearchedWord;
import com.example.demo.words.SearchedWordService;
import com.example.demo.words.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private WordsService wordsService;

    private SearchedWordService searchedWordService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    public QuizService(WordsService wordsService, SearchedWordService searchedWordService) {
        this.wordsService = wordsService;
        this.searchedWordService = searchedWordService;
    }

    // TODO*** wspolna funkcja do tworzenia quizow
    public Quiz createQuiz(QuizType quizType, QuizDataType quizDataType, IntFunction<List<Word>> getWordsFunction) {

        return null;
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
        return addToDatabase(quiz);
    }

    public Quiz createSearchedWordsQuiz() {
        Quiz quiz = new Quiz(QuizType.TRANSLATIONS, QuizDataType.PRESELECT);
        List<Word> searchedWords = getSearchedWords(10);
        try {
            prepareQuizQuestions(quiz, searchedWords);
        } catch (AnswerNotFoundException e) {
            System.out.println("Nie znaleziono odpowiedzi dla slowa o id " + e.getId());
            e.printStackTrace();
        }
        return addToDatabase(quiz);
    }

    private Quiz addToDatabase(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> findById(Long quizId) {
        return quizRepository.findById(quizId);
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

    private List<Word> getSearchedWords(int wordsCount) {

        List<SearchedWord> searchedWords = searchedWordService.getSearchedWords(wordsCount);

        List<Long> idsList = searchedWords.stream().map(searchedWord -> searchedWord.getWordId()).collect(Collectors.toList());

        List<Word> allById = wordsService.findAllById(idsList);
        return allById;
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
        if (randomWords.size() < 4) {
            System.out.println("Zbyt malo slow w quizie");
            return;
        }

        int numberOfWords = randomWords.size();

        // wyszukiwanie tlumaczen/znaczen
        for (Word randomWord : randomWords) {
            if (QuizType.TRANSLATIONS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(randomWord));
            } else if (QuizType.MEANINGS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(randomWord.getWord()));
            }
        }

        Random r = new Random();
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (int i = 0; i < numberOfWords; i++) {
            Set<Integer> chosenAnswersSet = new LinkedHashSet<>();
            // aktualnie wybrane/wylosowane odpowiedzi (juz uzyte)
            chosenAnswersSet.add(i); // dodanie numeru poprawnej odpowiedzi
            // losowanie 3 pozostalych odpowiedzi:
            while (chosenAnswersSet.size() != 4) {
                chosenAnswersSet.add(r.nextInt(numberOfWords));
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

    // TODO quizy ze znaczeniami

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
     * @param word wylosowane slowo dla ktorego pobieramy odpowiedz
     * @return znaleziona odpowiedz.
     */
    private String getAnswer(Word word) {
        Random random = new Random();
        List<Translation> translations = word.getTranslations();
        Translation translation = translations.get(random.nextInt(translations.size()));
        String quote = translation.getQuote();
        if (Type.IDIOM.equals(translation.getType())) {
            word.setWord(translation.getPhrase());
            return translation.getQuote();
        }
        return quote;
    }

    public List<Boolean> calculateResults(Quiz quiz, QuizAnswers quizAnswers) {
        List<Integer> correctAnswers = quiz.getQuizQuestions().stream()
                .map(question -> question.getCorrectAnswer()).toList();
        List<Integer> answersFromUser = quizAnswers.getAnswers();
        List<Boolean> results = new ArrayList<>();
        for (int i = 0; i < correctAnswers.size(); i++) {
            results.add(correctAnswers.get(i).equals(answersFromUser.get(i)));
        }

        return results;
    }
}