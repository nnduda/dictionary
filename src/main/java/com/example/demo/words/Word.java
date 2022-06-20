package com.example.demo.words;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "words")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private Phonetic[] phonetics;

    private Meaning [] meanings;

}
