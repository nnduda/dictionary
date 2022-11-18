package com.example.demo.words;

import com.example.demo.model.Note;
import com.example.demo.model.ReferenceTableType;
import com.example.demo.model.WordExtras;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    @Autowired
    NoteRepository noteRepository;

    public void addNote(Word word, String note) {
        addNote(word.getId(), ReferenceTableType.WORDS, note);
    }

    public void addNote(Translation translation, String note) {
        addNote(translation.getId(), ReferenceTableType.TRANSLATIONS, note);
    }

    public void addNote(WordExtras wordExtras, String note) {
        addNote(wordExtras.getId(), ReferenceTableType.WORDS, note);
    }

    // TODO ^dokonczyc na pozostale encje

    private void addNote(Long referenceTableId, ReferenceTableType referenceTableType, String note) {
        switch (referenceTableType) {
            case WORDS -> {
                Note noteWord = new Note(ReferenceTableType.WORDS, referenceTableId, note);
                noteRepository.save(noteWord);
            }
            case WORD_EXTRAS -> {
                Note noteWordExtras = new Note(ReferenceTableType.WORD_EXTRAS, referenceTableId, note);
                noteRepository.save(noteWordExtras);
            }
            case TRANSLATIONS -> {
                Note noteTranslations = new Note(ReferenceTableType.TRANSLATIONS, referenceTableId, note);
                noteRepository.save(noteTranslations);
            }
        }
        // TODO dokonczyc
    }
}
