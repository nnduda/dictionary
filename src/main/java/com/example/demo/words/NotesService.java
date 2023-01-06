package com.example.demo.words;

import com.example.demo.model.Note;
import com.example.demo.model.ReferenceTableType;
import com.example.demo.model.WordExtras;
import com.example.demo.model.xml.Translation;
import com.example.demo.model.xml.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        addNote(wordExtras.getId(), ReferenceTableType.WORD_EXTRAS, note);
    }

    private void addNote(Long referenceTableId, ReferenceTableType referenceTableType, String note) {
        Note noteWord = new Note(referenceTableType, referenceTableId, note);
        noteRepository.save(noteWord);
    }

    // TODO metoda find do wyszukiwania notatek
    // wyszukiwanie na podstawie slowa/tlumaczenia/dodatku - bo to bedziemy znali chcac zobaczyc notatke
    public void find(ReferenceTableType referenceTableType) { // , Long referenceTableId)
        List<Note> notes = noteRepository.findByReferenceTable(referenceTableType);
    }
}
