package com.example.demo.words;

import com.example.demo.model.xml.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsRepository extends JpaRepository<Word, Long> {

}
