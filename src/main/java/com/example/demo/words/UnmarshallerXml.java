package com.example.demo.words;

import com.example.demo.model.xml.Note;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class UnmarshallerXml {
    public static void main(String[] args) {
        UnmarshallerXml unmarshallerXml = new UnmarshallerXml();
        unmarshallerXml.convertXmlToObject("a");
    }
    // TODO zmapowac plik a.xml na klasy (tak żeby dało się go odczytywać i zapisywać do obiektu)
    // TODO wrzucic zmiany na GIT :)

    private final String FOLDER_NAME = "/static/";

    private void convertXmlToObject(String letter)
    {
        try
        {
            File file = new File("src/main/java/com/example/demo/words/file2.xml");
            //File file = new File(this.getClass().getResource(FOLDER_NAME + letter + ".xml").getPath());
            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            //JAXBContext jaxbContext = JAXBContext.newInstance(LetterEntries.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Note note = (Note) unmarshaller.unmarshal(file);

            if (note.getTo() != null) {
                if (note.getTo().getAttr() != null) {
                    if (note.getTo().getAttr().equals("attr")) {
                        // to co odczytamy po unmarshallingu zapiszemy do bazy danych (jako nowa encja)
                        // np.
                    }
                }
            }

            //LetterEntries letterEntries = (LetterEntries) unmarshaller.unmarshal(file);
            System.out.println(note);
            //System.out.println(letterEntries);

        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
