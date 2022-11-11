package com.example.demo.model;

import com.example.demo.words.WordsRepository;
import com.example.demo.words.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class WordExtrasService {
    WordsRepository wordsRepository;

    RestTemplate restTemplate;

    WordsService wordsService;

    @Autowired
    public WordExtrasService(WordsRepository wordsRepository, RestTemplate restTemplate) {
        this.wordsRepository = wordsRepository;
        this.restTemplate = restTemplate;
    }


}
