package com.example.demo.words;

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

    /*public List<SearchedWord> getSearchedWords(int wordsCount) {
        return searchedWordRepository.getLastCreatedSearchedWords(wordsCount);
    }*/

    public List<SearchedWord> getSearchedWords(int wordsCount) {
        // wordsCount = 10
        // searchedWordsHC = 20
        // 20% * wordsCount = 2
        // 80% losowe uprzednio wyszukiwane slowa
        // 20% slowa o najwyzszym counterze (losowo)
        Random random = new Random();
        List<SearchedWord> searchedWordsHC = searchedWordRepository.getSearchedWordsWithHighestCounter(wordsCount);
        Collections.shuffle(searchedWordsHC);
        Set<SearchedWord> searchedWords = new HashSet<>(Arrays.asList(searchedWordsHC.get(0), searchedWordsHC.get(1)));

        long numWords = searchedWordRepository.count();
        while (searchedWords.size() < wordsCount) {
            Optional<SearchedWord> searchedWord = searchedWordRepository.findById(random.nextLong() % numWords);
            searchedWord.ifPresent(sw -> searchedWords.add(sw));
        }

        return new ArrayList<>(searchedWords);
    }
}
