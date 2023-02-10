package com.example.demo.words;

import com.example.demo.exceptions.LetterEntryNotFoundException;
import com.example.demo.model.xml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@SuppressWarnings("unused")
public class UnmarshallerXml {
    @Autowired
    WordsService wordsService;
    private static final String FOLDER_NAME = "/static/";

    @PostConstruct
    private void postConstruct() {
        List<Word> entities = getEntities();
        wordsService.saveAndMergeDuplicates(entities);
    }

    private List<Word> getEntities() {
        List<LetterEntries> letterEntries = getAllLetterEntries();
        List<Word> entities = getEntitiesFromLetterEntries(letterEntries);
        return entities;
    }

    private List<LetterEntries> getAllLetterEntries() {
        List<LetterEntries> entries = new ArrayList<>();
        for (char letter = 'a'; letter <= 'z'; letter++) {
            try {
                LetterEntries letterEntries = convertXmlToObject(letter);
                entries.add(letterEntries);
            } catch (LetterEntryNotFoundException e) {
                e.printStackTrace();
            }
        }
        return entries;
    }

    private LetterEntries convertXmlToObject(char letter) throws LetterEntryNotFoundException {
        try {
            File file = new File(this.getClass().getResource(FOLDER_NAME + letter + ".xml").getPath());
            JAXBContext jaxbContext = JAXBContext.newInstance(LetterEntries.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            LetterEntries letterEntries = (LetterEntries) unmarshaller.unmarshal(file);
            System.out.println(letterEntries.getEntries()[0]);
            return letterEntries;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        throw new LetterEntryNotFoundException();
    }

    private List<Word> getEntitiesFromLetterEntries(List<LetterEntries> letterEntries) {
        List<Word> words = new ArrayList<>();
        for (LetterEntries letterEntry : letterEntries) {
            for (Entry entry : letterEntry.getEntries()) {
                Word word = new Word();
                List<Translation> translations = new ArrayList<>();
                loadFromForm(entry.getForm(), word);
                loadTranslationsFromSenses(translations, entry.getSenses(), entry.getGramGrp());

                word.setTranslations(translations);
                words.add(word);
            }
        }
        return words;
    }

    private void loadFromForm(Form form, Word word) {
        if (form != null) {
            word.setPronunciation(form.getPron());
            word.setWord(form.getOrth());
        }
    }

    private void loadFromGramGrp(GramGrp gramGrp, List<Translation> translations) {
        if (gramGrp != null) {
            String pos = gramGrp.getPos();
            translations.forEach(t -> t.setPartOfSpeech(pos));
        }
    }

    private void loadTranslationsFromSenses(List<Translation> translations, Sense[] senses, GramGrp previousGramGrp) {
        if (senses == null) {
            return;
        }
        for (Sense sense : senses) {
            Cit[] cits = sense.getCits();
            GramGrp gramGrp = sense.getGramGrp();
            gramGrp = Optional.ofNullable(gramGrp).orElse(previousGramGrp);

            List<Translation> translationsFromCits = getTranslationsFromCit(sense, cits);
            loadFromGramGrp(gramGrp, translationsFromCits);
            translations.addAll(translationsFromCits);
            loadTranslationsFromSenses(translations, sense.getSenses(), gramGrp);
        }
    }

    private List<Translation> getTranslationsFromCit(Sense sense, Cit[] cits) {
        List<Translation> translations = new ArrayList<>();
        if (cits != null) {
            for (Cit cit : cits) {
                Translation translation = new Translation();
                Type type = cit.getType();
                translation.setQuote(cit.getQuote());
                translation.setType(type);
                Xr xr = sense.getXr();
                // XR zawiera rozwiniecie skrotu
                if (xr != null && !type.equals(Type.COLLOC)) {
                    translation.setPhrase(xr.getRef());
                }
                if (Type.IDIOM.equals(type)) {
                    translation.setPhrase(cit.getQuote());
                    translation.setQuote(cit.getCit().getQuote());
                }
                translations.add(translation);
            }
        }
        return translations;
    }
}

