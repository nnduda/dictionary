package com.example.demo.words;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meaning {
    String[] synonyms;
    Definition [] definitions;
}
