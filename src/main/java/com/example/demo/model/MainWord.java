package com.example.demo.model;

import com.example.demo.model.json.Meaning;
import com.example.demo.model.json.Phonetic;
import com.example.demo.model.json.Word;
import com.example.demo.model.xml.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class MainWord {

    private String word;
    private String pronunciation;
    private List<Translation> translations;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    // stare rozwiazanie z List<List<Translation>>:
    // drink
    // Word[] -> {Word{id=1, word=drink, translation=pić}, Word{id=2, word=drink, translation=napój}}
    /*
    Word{word=drink, translation={pić}}
    Word{word=drink, translation={napój}}
    stream.map(w -> w.getTranslations()):
    {pić} (lista)
    {napój} (lista)
    collect(Collectors.toList()):
    {{pić},{napój}}

    select count(word), word
    from words
    group by word
    having count(word) > 1;
     */
    public MainWord(String word, com.example.demo.model.xml.Word wordFromDatabase, com.example.demo.model.json.Word[] wordsFromExternalApi) {
        this.word = word;
        if (Objects.nonNull(wordFromDatabase)) {
            this.pronunciation = wordFromDatabase.getPronunciation();
            this.translations = wordFromDatabase.getTranslations();
            this.phonetics = Arrays.stream(wordsFromExternalApi)
                    .map(w -> w.getPhonetics())
                    .flatMap(Arrays::stream) // Stream<Phonetic[]> -> Stream<Phonetic> // flatten
                    .collect(Collectors.toList());
            this.meanings = Arrays.stream(wordsFromExternalApi)
                    .map(Word::getMeanings)
                    .flatMap(Arrays::stream) // Stream<Phonetic[]> -> Stream<Phonetic> // flatten
                    .collect(Collectors.toList());
        }
    }
}
