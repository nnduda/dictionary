package com.example.demo.model.xml;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Translation {
    // TODO tak samo jak w word
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Type type; // TODO zrobic z tego enuma na TRANS, COLLOC itp.

    private String phrase; // ?

    private String quote; // ?

}
