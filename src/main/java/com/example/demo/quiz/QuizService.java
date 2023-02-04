package com.example.demo.quiz;

import com.example.demo.exceptions.AnswerNotFoundException;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizDataType;
import com.example.demo.model.QuizType;
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
        List<Long> wordsIds = setWordsAndGetWordsIds(quiz);
        try {
            prepareAnswers(quiz, wordsIds);
        } catch (AnswerNotFoundException e) {
            System.out.println("Nie znaleziono odpowiedzi dla slowa o id " + e.getId());
            e.printStackTrace();
        }
        return quiz;
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
        words.set(9, "binge");
        Word[] binges = wordsService.getWordsFromDatabase("binge");
        wordsIds.set(9, binges[0].getId());
        quiz.setWords(words);
        return wordsIds;
    }

    private void prepareAnswers(Quiz quiz, List<Long> wordsIds) throws AnswerNotFoundException { // wordsIds - identyfikatory slow: "dog","cat","turtle","lion","fish"
        List<String> correctAnswers = new ArrayList<>(); // poprawne odpowiedzi dla kolejnych slow np. lista: pies, kot, zolw, lew, ryba

        // wyszukiwanie tlumaczen/znaczen
        for (int i = 0; i < wordsIds.size(); i++) {
            if (QuizType.TRANSLATIONS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(wordsIds.get(i), i, quiz));
            } else if (QuizType.MEANINGS.equals(quiz.getQuizType())) {
                correctAnswers.add(getAnswer(quiz.getWords().get(i))); // TODO do zmiany analogicznie jak translations?
            }
        }

        Random r = new Random();
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


            int tab[] = {2, 6, 7, 8};
            int el1 = 0; // 0
            int el2 = 2; // correctAnswerNumber

            int tmp = tab[el2];
            tab[el2] = tab[el1];
            tab[el1] = tmp;

            /*
            // standardowy swap napisany recznie:
            tmp = chosenAnswers.get(correctAnswerNumber);
            chosenAnswers.set(correctAnswerNumber, chosenAnswers.get(0));
            chosenAnswers.set(0, tmp);
            */

            // jednolijkowy swap (brzydki):
            // chosenAnswers.set(0,(chosenAnswers.set(correctAnswerNumber, chosenAnswers.get(0))));

            // produkcyjne, najczytelniejsze, najkrotsze
            // Collections.swap(correctAnswers, 0, correctAnswerNumber);

            // tworczy pomysl, nie do konca swap:
            // 2,6,7,8
            // 6,7,8 (2 zapamietane - correctAnswer)
            // 6,7,2,8
            // add(indeks, wartosc) - dodaje wartosc pod dany indeks i przesuwa to co w nim bylo w prawo (razem z reszta elementow)
            Integer correctAnswer = chosenAnswers.remove(0);//  4,1,2
            chosenAnswers.add(correctAnswerNumber, correctAnswer); // 4,1,3,2


            List<String> answers = new ArrayList<>(); // wylosowane odpowiedzi do danego slowa
            for (Integer answerNumber : chosenAnswers) {
                answers.add(correctAnswers.get(answerNumber));
            }

            quiz.getAnswers().add(answers);
            quiz.getCorrectAnswers().add(correctAnswerNumber);
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
            Translation translation = translations.get(random.nextInt(translations.size()));
            String quote = translation.getQuote();
            if (Type.IDIOM.equals(translation.getType())) {
                quiz.setWord(wordIdx, translation.getPhrase()); // list.set(index, wartosc)
                //quiz.getWords().set(i, translation.getPhrase());
            }

            return quote;
        }
        throw new AnswerNotFoundException(wordId);
    }
}