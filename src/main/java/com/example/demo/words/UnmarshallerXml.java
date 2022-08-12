package com.example.demo.words;

import com.example.demo.exceptions.LetterEntryNotFoundException;
import com.example.demo.model.xml.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnmarshallerXml {
    // TODO mapowanie przy uzyciu MapStruct?
    public static void main(String[] args) {
        UnmarshallerXml unmarshallerXml = new UnmarshallerXml();

        //  unmarshallerXml.convertXmlToObject("a");
        List<LetterEntries> letterEntries = unmarshallerXml.getAllLetterEntries();
        unmarshallerXml.translateLetterEntriesToEntities(letterEntries);
    }

    // TODO metoda ktora zamienia List<LetterEntries> na wpisy w bazie danych
    // TODO List<LetterEntries> -> Word,Translation w bazie
    // TODO pierwszy krok: jak zamienic Entry na Word

    // TODO wrzucic zmiany na GIT :)

    private final String FOLDER_NAME = "/static/";

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

    private void translateLetterEntriesToEntities(List<LetterEntries> letterEntries) {
        for (LetterEntries letterEntry : letterEntries) {
            for (Entry entry : letterEntry.getEntries()) {
                Word word = new Word();
                Translation translation = new Translation();
                Form form = entry.getForm();
                if (form != null) {
                    word.setPronunciation(form.getPron());
                    word.setWord(form.getOrth());
                }
                GramGrp gramGrp = entry.getGramGrp();
                if (gramGrp != null) {
                    word.setPartOfSpeech(gramGrp.getPos());
                }
                // TODO sprobowac :)
                // TODO word.setPartOfSpeech();
                // TODO word.setWord();

                // TODO tlumaczenia:
                for (Sense sense : entry.getSenses()) {
                    for(Cit cit : sense.getCits()){
                        Type type = Type.valueOf(cit.getType());
                        translation.setQuote(cit.getQuote());
                        translation.setType(type);
                        Xr xr = sense.getXr();
                        if(xr!=null){
                            translation.setPhrase(xr.getRef());
                        }
                    }

                    }

                }
                // TODO word.setTranslations();
            }
        }
    }
}
