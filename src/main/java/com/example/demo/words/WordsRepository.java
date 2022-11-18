package com.example.demo.words;

import com.example.demo.model.xml.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordsRepository extends JpaRepository<Word, Long> {

    List<Word> findByWord(String word);

    List<Long> findIdsByWord(String word);

}
