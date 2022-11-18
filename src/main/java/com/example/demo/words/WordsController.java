package com.example.demo.words;

import com.example.demo.model.MainWord;
import com.example.demo.model.WordExtras;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("words")
public class WordsController {

    private WordsRepository wordsRepository;
    private WordsService wordsService;

    private WordExtrasRepository wordExtrasRepository;

    @Autowired
    public WordsController(WordsRepository wordsRepository, WordsService wordsService, WordExtrasRepository wordExtrasRepository) {
        this.wordsRepository = wordsRepository;
        this.wordsService = wordsService;
        this.wordExtrasRepository = wordExtrasRepository;
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

    /*
    Testujemy zapis do bazy danych
     */

    @GetMapping(value = "/test1")
    public ResponseEntity<String> save1() {
        // zapis slowa z powiazanymi informacjami
        Word word = new Word();
        word.setWord("abc");
        Translation translation = new Translation();
        translation.setPartOfSpeech("V");
        word.setTranslations(List.of(translation));
        WordExtras wordExtras = new WordExtras();
        wordExtras.setValue("abc");
        wordExtras.setWord(word);
        List<WordExtras> wordExtrasList = new ArrayList<>();
        wordExtrasList.add(wordExtras);
        //word.setWordExtrasList(List.of(wordExtras));
        word.setWordExtrasList(wordExtrasList);
        wordsRepository.save(word); // to zapisze do wszystkich tabel powiazanych
        // nie trzeba tworzyc ilus osobnych repozytoriow
        return ResponseEntity.ok(word.toString());
    }

    @GetMapping("/test2")
    public ResponseEntity<String> save2() {
        // aktualizacja slowa 'abc' - dodanie WordExtras
        Word[] words = wordsService.getWordsFromDatabase("abc");
        if (words != null) {
            Word word = words[0];
            WordExtras wordExtras = new WordExtras();
            wordExtras.setValue("def");
            wordExtras.setWord(word);
            // najpierw odczytujemy liste z elementami ktore juz sa w bazie
            List<WordExtras> wordExtrasList = word.getWordExtrasList();
            wordExtrasList.add(wordExtras); // dodajemy kolejny element
            // nie potrzebujemy robic set, bo operujemy caly czas na jednej liscie
            //word.setWordExtrasList(wordExtrasList);
            wordsRepository.save(word); //
            return ResponseEntity.ok(word.toString());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/test3")
    public ResponseEntity<String> save3() {
        Word[] words = wordsService.getWordsFromDatabase("abc");
        if (words != null) {
            Word word = words[0];
            WordExtras wordExtras = new WordExtras();
            wordExtras.setValue("ghi");
            wordExtras.setWord(word);
            wordExtrasRepository.save(wordExtras);

            // sprawdzenie czy dodalo sie poprawnie:
            words = wordsService.getWordsFromDatabase("abc");
            word = words[0];
            return ResponseEntity.ok(word.toString());
        }
        return ResponseEntity.notFound().build();
    }
}
