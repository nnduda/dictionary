package com.example.demo.quiz;

import com.example.demo.exceptions.WordsNotFoundException;
import com.example.demo.model.Note;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizAnswers;
import com.example.demo.model.QuizQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("quiz")
public class QuizController {

    private QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<Quiz> getQuiz() {
        Quiz randomQuiz = quizService.createRandomQuiz();
        return ResponseEntity.ok(randomQuiz);
    }

    @GetMapping("/random")
    public ModelAndView getRandomQuiz() {
        ModelAndView mv = new ModelAndView("quiz");
        Quiz randomQuiz = quizService.createRandomQuiz();
        mv.addObject("quiz", randomQuiz);
        // TODO Zamienic Note na jakis prosty obiekt (np. obiekt z jednym Stringiem i liczba) - nie ma to byc encja
        // potem podmienic wszedzie Note (kontroler + widok) na ten nowy obiekt
        // chodzi o to, zeby w Note nie bylo tych wszystkich niepotrzebnych pol typu id, referenceTableId itp., czyli zrobic obiekt transportowy
        mv.addObject("note", new Note("example note"));
        mv.addObject("quizAnswers", new QuizAnswers(randomQuiz.getId()));

        return mv;
    }

    @GetMapping("/searched")
    public ModelAndView getSearchedWordsQuiz() {
        ModelAndView mv;
        try {
            Quiz searchedWordsQuiz = quizService.createSearchedWordsQuiz();
            mv = new ModelAndView("quiz");
            mv.addObject("quiz", searchedWordsQuiz);
            mv.addObject("note", new Note("example note"));
            mv.addObject("quizAnswers", new QuizAnswers(searchedWordsQuiz.getId()));
        } catch (WordsNotFoundException e) {
            mv = new ModelAndView("error");
            mv.addObject("message", e.getMessage());
        }
        return mv;

    }

    @PostMapping("/postNote")
    public ResponseEntity<Note> postNote(@ModelAttribute("note") Note note) {

        return ResponseEntity.ok(note);
    }

    @PostMapping
    public ResponseEntity<Object> postAnswers(@ModelAttribute("quizAnswers") QuizAnswers quizAnswers) {
        Optional<Quiz> quizOpt = quizService.findById(quizAnswers.getQuizId());
        Quiz quiz = quizOpt.get();
        List<Boolean> results = quizService.calculateResults(quiz, quizAnswers);
        // slowo - wybrana odpowiedz (czerwona/zielona) - prawidlowa odpowiedz (tylko jesli wybrana jest zla)
        // results -> wyniki true/false -> wyswietlanie prawidlowej odpowiedzi jesli false + kolorki
        // quiz -> z quizQuestion wyciagamy prawidlowa odpowiedz na podstawie correctAnswer (numer) i answers (lista)
        // quizAnswers -> wybrane odpowiedzi przez uzytkownika (numery) -> slowa do wyciagniecia z answers z quiz'u
        List<String> userAnswers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();
        List<Integer> answers = quizAnswers.getAnswers();
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
        for (int i = 0; i < quizQuestions.size(); i++) {
            QuizQuestion quizQuestion = quizQuestions.get(i);
            List<String> quizQuestionAnswers = quizQuestion.getAnswers();
            Integer userAnswerNumber = answers.get(i);
            int correctAnswerNumber = quizQuestion.getCorrectAnswer();
            if (userAnswerNumber.equals(correctAnswerNumber)) {
                correctAnswers.add("");
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

        // TODO ^do przeniesienia do calculateResults i moze do zapakowania w odrebny obiekt (np. Result)

        return ResponseEntity.ok(results);
    }
}

