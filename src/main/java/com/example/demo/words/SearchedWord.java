package com.example.demo.words;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchedWord that = (SearchedWord) o;
        // bez counter bo on teoretycznie moglby sie zmienic w trakcie
        return Objects.equals(id, that.id) && Objects.equals(wordId, that.wordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wordId);
    }
}
