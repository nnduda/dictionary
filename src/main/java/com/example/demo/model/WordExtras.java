package com.example.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "word_extras")
@Data
public class WordExtras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ExtrasType extrasType;

    private String value;

    // TODO stworzyc serwis ktory potrafi zapisywac te dane
    // stworzyc endpoint do testowania czy dziala
    // + jak dodawac slowa i tlumaczenia, i w jakim serwisie
}
