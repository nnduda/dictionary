package com.example.demo.words;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class UnmarshallerXml {
    public static void main(String[] args) {
        convertXmlToObject();
    }
    private static void convertXmlToObject()
    {
        try
        {
            File file = new File("src/main/java/com/example/demo/words/file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(File.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Word word = (Word) unmarshaller.unmarshal(file);
            System.out.println(word);

        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
