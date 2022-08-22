package com.example.demo.words;

import com.example.demo.model.xml.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordsService {
    @Autowired
    WordsRepository wordsRepository;
    UnmarshallerXml unmarshallerXml;

    @Autowired
    public void setUnmarshallerXml(UnmarshallerXml unmarshallerXml) {
        this.unmarshallerXml = unmarshallerXml;
        List<Word> entities = unmarshallerXml.getEntities();
        wordsRepository.saveAll(entities);
    }
}
