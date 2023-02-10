package com.example.demo.model.xml;

import com.example.demo.model.WordExtras;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity(name = "words")
@Data
public class Word {
    // reprezentacja xml'a w bazie danych
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String pronunciation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id") // kolumna z tablicy translations
    private List<Translation> translations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "word")
    @JsonManagedReference
    private List<WordExtras> wordExtrasList;

    public void addTranslations(List<Translation> translations) {
        this.translations.addAll(translations); // laczymy wszystko (z duplikatami)
        Set<Translation> translationSet = new HashSet<>(this.translations); // usuwamy duplikaty
        this.translations = new ArrayList<>(translationSet); // zapisujemy ponownie bez duplikatow
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return word.equals(word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}

// id, id slowa, typ wartosci, wartosc
// 1, 13, przyklad, Ala ma kota
// 2, 13, synonim, ssak
// 3, 123, audio, ""

// ^ select * from __ where id_slowa = 13

