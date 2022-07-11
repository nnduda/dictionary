package com.example.demo.words;

import com.example.demo.model.json.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("words")
public class WordsController {

    private WordsRepository wordsRepository;

    @Autowired
    public WordsController(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }

    @GetMapping("/test")
    public String test() {

        return "abc";
    }

    @GetMapping()
    public String getWords(Model model){
        model.addAttribute("words", wordsRepository.findAll());
        return "words"; // words.html
        // jesli chcelibysmy uzyc metody 3 (bez podawania sciezki) potrzebny bedzie template resolver
    }

    @PostMapping("/add")
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
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
    public ResponseEntity<Word[]> getFromExternalApiToObject(@RequestParam String name) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + name;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Word[]> response = restTemplate.getForEntity(url, Word[].class);
        return response;
    }
}
