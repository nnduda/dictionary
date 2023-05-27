package com.example.demo.quiz;

import com.example.demo.model.Note;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        ModelAndView mv = new ModelAndView("quiz");
        Quiz searchedWordsQuiz = quizService.createSearchedWordsQuiz();
        mv.addObject("quiz", searchedWordsQuiz);
        mv.addObject("note", new Note("example note"));
        mv.addObject("quizAnswers", new QuizAnswers(searchedWordsQuiz.getId()));

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
        return ResponseEntity.ok(results);
    }
}

