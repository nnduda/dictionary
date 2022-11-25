package com.example.demo.model;

import com.example.demo.model.xml.Word;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "word_extras")
@Data
public class WordExtras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ExtrasType extrasType;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    @JsonBackReference
    //@JsonIgnore // alternatywnie zamiast Json*Reference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Word word;

}
