package com.example.demo.words;

import com.example.demo.model.xml.LetterEntries;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class UnmarshallerXml {
    public static void main(String[] args) {
        UnmarshallerXml unmarshallerXml = new UnmarshallerXml();

        unmarshallerXml.convertXmlToObject("a");
    }
    
    // TODO metoda do napisania ktora zwraca List<LetterEntries> (dane dla wszystkich literek)
    // TODO metoda ktora zamienia List<LetterEntries> na wpisy w bazie danych
    // TODO List<LetterEntries> -> Word,Translation w bazie
    // TODO pierwszy krok: jak zamienic Entry na Word

    // TODO wrzucic zmiany na GIT :)

    private final String FOLDER_NAME = "/static/";

    private void convertXmlToObject(String letter)
    {
        try
        {
            File file = new File(this.getClass().getResource(FOLDER_NAME + letter + ".xml").getPath());
            JAXBContext jaxbContext = JAXBContext.newInstance(LetterEntries.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            LetterEntries letterEntries = (LetterEntries) unmarshaller.unmarshal(file);
            System.out.println(letterEntries.getEntries()[0]);

        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
