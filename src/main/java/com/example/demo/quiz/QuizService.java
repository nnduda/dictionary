package com.example.demo.quiz;

import com.example.demo.model.Quiz;
import com.example.demo.model.QuizDataType;
import com.example.demo.model.QuizType;
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
        List<Long> wordsIds = setWordsAndGetWordsIds(quiz);
        prepareAnswers(quiz, wordsIds);
        return quiz;
    }

    private List<Long> setWordsAndGetWordsIds(Quiz quiz) {
        List<String> words = new ArrayList<>();
        List<Long> wordsIds = new ArrayList<>();
        Random random = new Random();
        while (words.size() < 10) {
            Optional<Word> word = wordsService.getWordById(random.nextLong());
            if (word.isPresent()) {
                words.add(word.get().getWord());
                wordsIds.add(word.get().getId());
            }
        }
        /*

        for (int i = 0; i < 10; ) {
            Optional<Word> word = wordsService.getWordById(random.nextLong());
            if (word.isPresent()) {
                words.add(word.get());
                i++;
            }
        }
            return quiz;
*/
        quiz.setWords(words);
        return wordsIds;
    }

    // TODO spojrzec na kod i przypomniec sobie jak dziala
    private void prepareAnswers(Quiz quiz, List<Long> wordsIds) { // wordsIds - identyfikatory slow: "dog","cat","turtle","lion","fish"
        List<String> answers = new ArrayList<>(); // wylosowane odpowiedzi do danego slowa
        int correctAnswerNumber = -1; // numer poprawnej odpowiedzi (z listy answers)
        List<String> correctAnswers = new ArrayList<>(); // poprawne odpowiedzi dla kolejnych slow np. lista: pies, kot, zolw, lew, ryba
        Random r = new Random();

        // wyszukiwanie tlumaczen/znaczen
        for (int i = 0; i < wordsIds.size(); i++) {
            if (QuizType.TRANSLATIONS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(wordsIds.get(i)));
            } else if (QuizType.MEANINGS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(quiz.getWords().get(i)));
            }
        }

        for (int i = 0; i < wordsIds.size(); i++) {
            Set<Integer> chosenAnswersSet = new HashSet<>(); // ew. LinkedHashSet
            // aktualnie wybrane/wylosowane odpowiedzi (juz uzyte)
            chosenAnswersSet.add(i); // dodanie numeru poprawnej odpowiedzi
            // losowanie 3 pozostalych odpowiedzi:
            while (chosenAnswersSet.size() != 4) {
                chosenAnswersSet.add(r.nextInt(wordsIds.size()));
            }
            List<Integer> chosenAnswers = new ArrayList<>(chosenAnswersSet);
            // 3,4,1,2
            correctAnswerNumber = r.nextInt(4); // 2
            Integer correctAnswer = chosenAnswers.remove(0);//  4,1,2
            chosenAnswers.add(correctAnswerNumber, correctAnswer); // 4,1,3,2

            for (Integer answerNumber : chosenAnswers) {
                answers.add(correctAnswers.get(answerNumber));
            }

            quiz.getAnswers().add(answers);
            // przechodzimy po kazdym slowie/dla kazdego slowa i tworzymy liste odpowiedzi do niego (answers)
            // przygotowujemy dla danego slowa liste "answers"
            // na miejscu 'i' jest odpowiedz dla danego slowa w correctAnswers
            // trzeba wylosowac pozostale 3 odpowiedzi
        }



       /* String correctAnswer = correctAnswers.get(r.nextInt(correctAnswers.size()));
        answers.add(correctAnswer);

        List<Word> words = wordsService.getWords();

        Word word = wordsService.getWords().get(r.nextInt(words.size()));
        String wordFromList = word.getWord();
        answers.add(wordFromList);// x3?
        }*/


        int i = 0;
        // slowo = quiz.getWords().get(i)
        // tlumaczenie = correctAnswers.get(i)
        // odpowiedzi = {tlumaczenie, ..., ..., ...}

        /*
        words:
        kot
        pies
        lew
        ryba
        waz
        dzik

        correctAnswers:
        cat
        dog
        lion
        fish
        snake
        boar

        zadanie: wybranie odpowiedzi do slowa kot (idx 0)
        wiemy od razu ze poprawne jest cat (idx 0)
        correctAnswers.remove(0) - cat
        correctAnswers.remove(1) - dog
        correctAnswers.remove(2) - lion
        correctAnswers.remove(3) - fish

        ^to zadziala, ale tylko raz. usunie calkowicie z listy, wiec nie uzyjemy dla dalszych slow
        pies (idx 1)
        boar (idx 1) <- tu sie sypie, bo to nie jest odpowiednie tlumaczenie

        usuwanie z listy jest !*okej*!, ale trzeba by bylo zrobic kopie tej listy

        mozna losowac numerek odpowiedzi
        numerkiOdpowiedzi = []
        lew (idx 2) -> numerkiOdpowiedzi = [2] <- to wiemy od razu
        szukamy kolejnych
        losujemy nr 4 -> numerkiOdpowiedzi = [2,4]
        losujemy nr 2 -> numerkiOdpowiedzi = [2,4]
        ...
        numerkiOdpowiedzi = [2*,4,1,5]
        prawidlowa odpowiedz jest z przodu (miejsce 0) - dobrze ja przesunac

        mozna ja przesunac losowo w prawo w tablicy/liscie (od 0 do 3)
        losujemy 2:
        numerkiOdpowiedzi = [4,1,2,5]
        odpowiedzi(answers) = [snake, dog, lion, boar]
        numerPoprawnej = 2

         */

        //wordsIds.get(0); // id slowa
        // dla tego slowa szukasz poprawnej odpowiedzi
        // szukasz 3 kolejnych odpowiedzi, niepoprawnych
        // laczysz w calosc, wybierajac na ktorym miejscu bedzie dobra odpowiedz
        // powtarzasz calosc 10 razy
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

    // TODO

    /**
     * Pobranie odpowiedzi dla slowa o podanym wordId z bazy danych.
     *
     * @param wordId identyfikator slowa.
     * @return znaleziona odpowiedz.
     */
    private String getAnswer(Long wordId) {
        return null;
    }
}