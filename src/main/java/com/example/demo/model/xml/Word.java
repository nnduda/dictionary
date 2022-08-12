package com.example.demo.model.xml;

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

    private String partOfSpeech;

    private String pronunciation;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id") // kolumna z tablicy translations
    private List<Translation> translations;

}
