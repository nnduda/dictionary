package com.example.demo.words;

import com.example.demo.model.MainWord;
import com.example.demo.model.json.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class WordsService {

    private final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    WordsRepository wordsRepository;

    RestTemplate restTemplate;

    @Autowired
    public WordsService(WordsRepository wordsRepository, RestTemplate restTemplate) {
        this.wordsRepository = wordsRepository;
        this.restTemplate = restTemplate;
    }

    public MainWord getWord(String word) {
        com.example.demo.model.xml.Word wordsFromDatabase = getWordFromDatabase(word);
        com.example.demo.model.json.Word[] wordsFromExternalApi = getWordsFromExternalApi(word);
        MainWord mainWord = new MainWord(word, wordsFromDatabase, wordsFromExternalApi);
        return mainWord;
    }

    @Nullable
    public com.example.demo.model.xml.Word getWordFromDatabase(String name) {
        com.example.demo.model.xml.Word word = wordsRepository.findOneByWord(name);
        return word;
    }

    public com.example.demo.model.json.Word[] getWordsFromExternalApi(String name) {
        String url = DICTIONARY_API_URL + name;
        com.example.demo.model.json.Word[] words = null;
        try {
            words = restTemplate.getForEntity(url, com.example.demo.model.json.Word[].class).getBody();
        } catch (RestClientException e) {
            System.out.println("Failed to establish connection with remote server");
        }
        if (isNull(words)) {
            words = new Word[0];
        }
        return words;
    }

    public com.example.demo.model.xml.Word save(com.example.demo.model.xml.Word word) {
        return wordsRepository.save(word);
    }

    // przyklad
    public void saveWithNote(com.example.demo.model.xml.Word word, String note) {
        NotesService notesService = new NotesService();
        notesService.addNote(word, note);
    }

    // zadanie - ciekawostka
    // jak wyciagnac z bazy wylacznie jedno pole
    public String getPronunciation(String word) {
        String pronunciationByWord = wordsRepository.findPronunciationByWord(word);
        return pronunciationByWord;
    }

    /**
     * Get word from database by id.
     *
     * @param id word id.
     * @return Word from database.
     */
    public Optional<com.example.demo.model.xml.Word> getWordById(Long id) {

        // wordsRepository.findById(id); - wyszukuje cale slowo (wszystkie kolumny dla danego rekordu)
        // jak pobrac wartosc z jednej kolumny?
        return wordsRepository.findById(id);
    }

    public List<com.example.demo.model.xml.Word> getWords() {
        return wordsRepository.findAll();
    }

    public long countWords() {
        return wordsRepository.count();
    }

    public void saveAndMergeDuplicates(List<com.example.demo.model.xml.Word> words) {
/*
        // rozwiazanie na liscie
        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                if (words.get(i).equals(words.get(j))) {
                    words.get(j).addTranslations(words.get(i).getTranslations());
                    words.remove(i);
                }
            }
        }
*/

        /*
        0 - 1,2,3,4,5...
        1 - 2,3,4,5...
        2 - 3,4,5...
         */

        // rozwiazanie na mapie v1
        /*Map<String, com.example.demo.model.xml.Word> wordHashMap = new HashMap();
        for (com.example.demo.model.xml.Word word : words) {
            String wordValue = word.getWord();
            if (wordHashMap.containsKey(wordValue)) { // merge
                com.example.demo.model.xml.Word currentWord = wordHashMap.get(wordValue);
                word.addTranslations(currentWord.getTranslations());
            }
            wordHashMap.put(wordValue, word);
        }

        words.clear();
        words.addAll(wordHashMap.values());*/

        // rozwiazanie na mapie v2 (z merge)
        Map<String, com.example.demo.model.xml.Word> wordHashMap = new HashMap<>();
        for (com.example.demo.model.xml.Word word : words) {
            String wordValue = word.getWord();
            wordHashMap.merge(wordValue, word, com.example.demo.model.xml.Word::merge);
        }


        words.clear();
        words.addAll(wordHashMap.values());
        /*Map<String, Long> wordHashMap = new HashMap<>();
        for (com.example.demo.model.xml.Word word : words) {
            if (wordHashMap.containsKey(word.getWord())) {

                // cat - 15
                // dog - 30
                // snake - 45

                // cat - 60



                Long aLong = wordHashMap.get(word.getWord());

            } else
                wordHashMap.put(word.getWord(), word.getId());
        }*/

        // TODO* zmiana rozwiazania z HashMapą na strumien
        Map<String, com.example.demo.model.xml.Word> wordHashMap2 = words.stream()
                .collect(Collectors.toMap(
                        word -> word.getWord(),
                        word -> word,
                        com.example.demo.model.xml.Word::merge)
                );
        Word word = new Word();


        wordsRepository.saveAll(words);
    }

}


