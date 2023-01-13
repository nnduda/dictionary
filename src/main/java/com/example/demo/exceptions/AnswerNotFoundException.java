package com.example.demo.exceptions;

public class AnswerNotFoundException extends Exception {

    private Long id;

    public AnswerNotFoundException(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
