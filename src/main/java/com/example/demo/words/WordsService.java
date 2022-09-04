package com.example.demo.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordsService {
    @Autowired
    WordsRepository wordsRepository;

    
}
