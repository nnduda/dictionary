package com.example.demo.model;

import com.example.demo.model.xml.Word;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "word_extras")
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class WordExtras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ExtrasType extrasType;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    @Override
    public String toString() {
        return "WordExtras{" +
                "id=" + id +
                ", extrasType=" + extrasType +
                ", value='" + value +
                '}';
    }

    // TODO stworzyc serwis ktory potrafi zapisywac te dane
    // stworzyc endpoint do testowania czy dziala
    // + jak dodawac slowa i tlumaczenia, i w jakim serwisie
}
