package com.example.demo.exceptions;

public class WordsNotFoundException extends RuntimeException {

    public WordsNotFoundException(String message) {
        super(message);
    }
}
