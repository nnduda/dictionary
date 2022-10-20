package com.example.demo.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meaning {
    String partOfSpeech;
    String[] synonyms;
    Definition[] definitions;
}
