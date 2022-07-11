package com.example.demo.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "words") // TODO do zastanowienia, czy jsona chcemy zapisywac w bazie (czy tylko xml)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private Phonetic[] phonetics;

    private Meaning[] meanings;

}
