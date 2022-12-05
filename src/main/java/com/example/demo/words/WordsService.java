package com.example.demo.words;

import com.example.demo.model.MainWord;
import com.example.demo.model.json.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class WordsService {

    private final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    WordsRepository wordsRepository;

    RestTemplate restTemplate;

    @Autowired
    public WordsService(WordsRepository wordsRepository, RestTemplate restTemplate) {
        this.wordsRepository = wordsRepository;
        this.restTemplate = restTemplate;
    }

    public MainWord getWord(String word) {
        com.example.demo.model.xml.Word[] wordsFromDatabase = getWordsFromDatabase(word);
        com.example.demo.model.json.Word[] wordsFromExternalApi = getWordsFromExternalApi(word);
        MainWord mainWord = new MainWord(word, wordsFromDatabase, wordsFromExternalApi);
        return mainWord;
    }

    public com.example.demo.model.xml.Word[] getWordsFromDatabase(String name) {
        List<com.example.demo.model.xml.Word> words = wordsRepository.findByWord(name);
        return words.toArray(new com.example.demo.model.xml.Word[0]);
    }

    public com.example.demo.model.json.Word[] getWordsFromExternalApi(String name) {
        String url = DICTIONARY_API_URL + name;
        com.example.demo.model.json.Word[] words = restTemplate.getForEntity(url, com.example.demo.model.json.Word[].class).getBody();
        if (isNull(words)) {
            words = new Word[0];
        }
        return words;
    }

    public com.example.demo.model.xml.Word save(com.example.demo.model.xml.Word word) {
        return wordsRepository.save(word);
    }

    // przyklad
    public void saveWithNote(com.example.demo.model.xml.Word word, String note) {
        NotesService notesService = new NotesService();
        notesService.addNote(word, note);
    }

    // TODO zastanowic sie czy nie da sie wyciagnac z bazy wylacznie jednego pola
    public Optional<com.example.demo.model.xml.Word> getWordById(Long id) {
        return wordsRepository.findById(id);
    }

}


