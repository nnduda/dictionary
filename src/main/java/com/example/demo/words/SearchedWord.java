package com.example.demo.words;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "searched_word")
@Data
@NoArgsConstructor
public class SearchedWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    private Long counter;

    public SearchedWord(Long wordId) {
        this.wordId = wordId;
        this.counter = 0L;
    }

    public void increaseCounter() {
        this.counter += 1;
    }
}
