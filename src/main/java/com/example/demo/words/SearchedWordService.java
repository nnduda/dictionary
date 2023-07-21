package com.example.demo.words;

import com.example.demo.exceptions.WordsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchedWordService {

    private SearchedWordRepository searchedWordRepository;

    @Autowired
    public SearchedWordService(SearchedWordRepository searchedWordRepository) {
        this.searchedWordRepository = searchedWordRepository;
    }

    public void saveSearchedId(Long id) {
        Optional<SearchedWord> searchedWordOpt = searchedWordRepository.findByWordId(id);
        SearchedWord searchedWord = searchedWordOpt.orElse(new SearchedWord(id));
        searchedWord.increaseCounter();
        searchedWordRepository.save(searchedWord);
    }

    public List<SearchedWord> getSearchedWords(int wordsCount) {
        Random random = new Random();
        List<SearchedWord> searchedWordsHC = searchedWordRepository.getSearchedWordsWithHighestCounter(wordsCount);
        if (searchedWordsHC.size() < wordsCount) {
            throw new WordsNotFoundException("Not enough words to create quiz from searched words");
        }
        Collections.shuffle(searchedWordsHC);
        // TODO do zastanowienia czy nie pobierac wiekszej liczby poprzednio wyszukiwanych slow do quizu na podstawie wordsCount
        Set<SearchedWord> searchedWords = new HashSet<>(Arrays.asList(searchedWordsHC.get(0), searchedWordsHC.get(1)));
        long numWords = searchedWordRepository.count();
        while (searchedWords.size() < wordsCount) {
            Optional<SearchedWord> searchedWord = searchedWordRepository.findById(random.nextLong() % numWords); // TODO do zastanowienia co jesli sa dziury w id'kach
            searchedWord.ifPresent(sw -> searchedWords.add(sw));
        }

        return new ArrayList<>(searchedWords);
    }


    private long countSearchedWords() {
        return searchedWordRepository.count();
    }
}
