package com.example.demo.quiz;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        mv.addObject("quiz", quizService.createRandomQuiz());
        // TODO Zamienic Note na jakis prosty obiekt (np. obiekt z jednym Stringiem i liczba) - nie ma to byc encja
        // potem podmienic wszedzie Note (kontroler + widok) na ten nowy obiekt
        mv.addObject("note", new Note("example note"));
        mv.addObject("quizAnswers", new QuizAnswers());
        return mv;
    }

    @PostMapping
    public ResponseEntity<Note> postNote(@ModelAttribute("note") Note note) {

        return ResponseEntity.ok(note);
    }

    @PostMapping("/random") // TODO zmiana nazwy?
    public ResponseEntity<Object> postAnswers(@ModelAttribute("quizAnswers") QuizAnswers quizAnswers) {
        Quiz quiz = new Quiz(QuizType.TRANSLATIONS, QuizDataType.RANDOM);
        quizService.calculateResults(quiz, quizAnswers);
        return ResponseEntity.ok(quizAnswers.getAnswers());
    }
}

// cos do wyswietlania wyników
