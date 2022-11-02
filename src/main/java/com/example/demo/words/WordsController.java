package com.example.demo.words;

import com.example.demo.model.MainWord;
import com.example.demo.model.xml.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("words")
public class WordsController {

    private WordsRepository wordsRepository;
    private WordsService wordsService;

    @Autowired
    public WordsController(WordsRepository wordsRepository, WordsService wordsService) {
        this.wordsRepository = wordsRepository;
        this.wordsService = wordsService;
    }

    @GetMapping()
    public String getWords(Model model) {
        model.addAttribute("words", wordsRepository.findAll());
        return "words"; // words.html
        // jesli chcelibysmy uzyc metody 3 (bez podawania sciezki) potrzebny bedzie template resolver
    }

    @GetMapping(value = "/word")
    public ResponseEntity<MainWord> getWord(@RequestParam("word") String word) {
        MainWord mainWord = wordsService.getWord(word);
        return ResponseEntity.ok(mainWord);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Word> getWord(@PathVariable("id") Long id) {
        Optional<Word> word = wordsRepository.findById(id);
        if (word.isPresent()) {
            //return ResponseEntity.status(HttpStatus.FOUND).body(word.get());
            return ResponseEntity.ok(word.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            // return ResponseEntity.notFound().build(); // krocej
        }
        // krotsze:
        /*return word
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());*/
    }

    @PostMapping
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
        word.setId(null); // zawsze chcemy null, niezaleznie co tam jest
        wordsRepository.save(word);
        return ResponseEntity.ok(word);
    }

    @GetMapping("/translations")
    public ResponseEntity<String> getFromExternalApi(@RequestParam String name) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + name;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    @GetMapping("/translationsObject")
    public ResponseEntity<com.example.demo.model.json.Word[]> getFromExternalApiToObject(@RequestParam String name) {
        com.example.demo.model.json.Word[] words = wordsService.getWordsFromExternalApi(name);
        return ResponseEntity.of(Optional.of(words));
    }
}
