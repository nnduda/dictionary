package com.example.demo.model.xml;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "translations")
@Data
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Type type;

    private String phrase;

    private String quote;

    private String partOfSpeech;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return type == that.type && Objects.equals(phrase, that.phrase) && quote.equals(that.quote) && Objects.equals(partOfSpeech, that.partOfSpeech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, phrase, quote, partOfSpeech);
    }
}
