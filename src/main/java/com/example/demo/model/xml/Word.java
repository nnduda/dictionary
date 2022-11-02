package com.example.demo.model.xml;

import com.example.demo.model.WordExtras;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private List<WordExtras> wordExtrasList;
}

// id, id slowa, typ wartosci, wartosc
// 1, 13, przyklad, Ala ma kota
// 2, 13, synonim, ssak
// 3, 123, audio, ""

// ^ select * from __ where id_slowa = 13

