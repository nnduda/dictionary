package com.example.demo.words;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchedWordRepository extends JpaRepository<SearchedWord, Long> {

    Optional<SearchedWord> findByWordId(Long wordId);

    @Query(value = "SELECT * FROM searched_word ORDER BY counter DESC LIMIT :wordNum*2", nativeQuery = true)
    List<SearchedWord> getSearchedWordsWithHighestCounter(@Param("wordNum") int wordNum);
}
