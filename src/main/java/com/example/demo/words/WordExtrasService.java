package com.example.demo.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class WordExtrasService {
    WordsRepository wordsRepository;

    RestTemplate restTemplate;

    WordsService wordsService;

    // TODO ^zastanowic sie ktore pola faktycznie sa potrzebne
    
    @Autowired
    public WordExtrasService(WordsRepository wordsRepository, RestTemplate restTemplate) {
        this.wordsRepository = wordsRepository;
        this.restTemplate = restTemplate;
    }


}
