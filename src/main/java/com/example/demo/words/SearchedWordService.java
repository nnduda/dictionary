package com.example.demo.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return searchedWordRepository.getLastCreatedSearchedWords(wordsCount);
    }
}
