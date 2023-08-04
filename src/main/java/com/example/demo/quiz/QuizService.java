package com.example.demo.quiz;

import com.example.demo.exceptions.AnswerNotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Type;
import com.example.demo.model.xml.Word;
import com.example.demo.results.Result;
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

    public Quiz createRandomQuiz() {
        return createQuiz(QuizType.TRANSLATIONS, QuizDataType.RANDOM, wordsCount -> getRandomWords(wordsCount));
    }

    public Quiz createSearchedWordsQuiz() {
        return createQuiz(QuizType.TRANSLATIONS, QuizDataType.PRESEARCHED, this::getSearchedWords);
    }

    private Quiz createQuiz(QuizType quizType, QuizDataType quizDataType, IntFunction<List<Word>> getSelectedWordsFunction) {
        Quiz quiz = new Quiz(quizType, quizDataType);
        List<Word> selectedWords = getSelectedWordsFunction.apply(10);

        try {
            prepareQuizQuestions(quiz, selectedWords);
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
        quiz.getQuizQuestions().addAll(quizQuestions);

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

    // slowo - wybrana odpowiedz (czerwona/zielona) - prawidlowa odpowiedz (tylko jesli wybrana jest zla)
    // results -> wyniki true/false -> wyswietlanie prawidlowej odpowiedzi jesli false + kolorki
    // quiz -> z quizQuestion wyciagamy prawidlowa odpowiedz na podstawie correctAnswer (numer) i answers (lista)
    // quizAnswers -> wybrane odpowiedzi przez uzytkownika (numery) -> slowa do wyciagniecia z answers z quiz'u

    /**
     * TODO
     *  List<Integer> answers = quizAnswers.getAnswers();
     */
    public Result calculateResults(Quiz quiz, QuizAnswers quizAnswers) {

        List<String> questions = new ArrayList<>();
        List<String> userAnswers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();

        List<Integer> answers = quizAnswers.getAnswers();
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
        int counter = 0;
        for (int i = 0; i < quizQuestions.size(); i++) {
            QuizQuestion quizQuestion = quizQuestions.get(i);
            questions.add(quizQuestion.getWord());
            List<String> quizQuestionAnswers = quizQuestion.getAnswers();
            Integer userAnswerNumber = answers.get(i);
            int correctAnswerNumber = quizQuestion.getCorrectAnswer();
            if (userAnswerNumber.equals(correctAnswerNumber)) {
                correctAnswers.add("");
                counter += 1;
            } else {
                correctAnswers.add(quizQuestionAnswers.get(correctAnswerNumber));
            }
            if (userAnswerNumber == -1) {
                userAnswers.add("-");
            } else {
                String userAnswer = quizQuestionAnswers.get(userAnswerNumber);
                userAnswers.add(userAnswer);
            }
        }

        Result result = new Result(questions, userAnswers, correctAnswers, counter);
        return result;
    }
}