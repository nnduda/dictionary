package com.example.demo.words;

import com.example.demo.model.WordExtras;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordExtrasRepository extends JpaRepository<WordExtras, Long> {
}
