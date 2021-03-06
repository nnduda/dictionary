package com.example.demo.model.xml;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
public class Word {
    // reprezentacja xml'a w bazie danych
    // TODO adnotacje (m.in. moze gettery/settery)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String partOfSpeech;

    private String pronunciation;

    private List<Translation> translations;

}
