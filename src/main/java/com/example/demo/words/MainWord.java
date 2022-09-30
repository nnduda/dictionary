package com.example.demo.words;

import com.example.demo.model.json.Meaning;
import com.example.demo.model.json.Phonetic;
import com.example.demo.model.xml.Translation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MainWord {

    private String word;
    private List<String> partOfSpeech;
    private String pronunciation;
    private List<List<Translation>> translations;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    public MainWord(String word, com.example.demo.model.xml.Word[] wordsFromDatabase, com.example.demo.model.json.Word[] wordsFromExternalApi) {
        this.word = word;
        if (wordsFromDatabase.length != 0) {
            this.partOfSpeech = Arrays.stream(wordsFromDatabase)
                    .map(w -> w.getPartOfSpeech())
                    .collect(Collectors.toList());
            this.pronunciation = wordsFromDatabase[0].getPronunciation();
            this.translations = Arrays.stream(wordsFromDatabase)
                    .map(w -> w.getTranslations())
                    .collect(Collectors.toList());
            this.phonetics = Arrays.stream(wordsFromExternalApi)
                    .map(w -> w.getPhonetics())

                    .collect(Collectors.toList());

            this.meanings = Arrays.stream(wordsFromExternalApi)
                    .map(w -> w.getMeanings())


        }
    }
}
