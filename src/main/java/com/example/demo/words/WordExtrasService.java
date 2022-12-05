package com.example.demo.words;

import com.example.demo.model.WordExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordExtrasService {
    WordExtrasRepository wordExtrasRepository;

    @Autowired
    public WordExtrasService(WordExtrasRepository wordExtrasRepository) {
        this.wordExtrasRepository = wordExtrasRepository;
    }

    public WordExtras save(WordExtras wordExtras) {
        return wordExtrasRepository.save(wordExtras);
    }


}
