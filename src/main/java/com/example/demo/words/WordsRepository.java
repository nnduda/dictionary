package com.example.demo.words;

import com.example.demo.model.xml.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordsRepository extends JpaRepository<Word, Long> {

    List<Word> findByWord(String word);

    List<Long> findIdsByWord(String word);

    List<Word> getWords();

    // TODO przyklady:
    // pojedyncza wartosc kolumny:
    // 1. z uzyciem Query - w Query da sie stworzyc obiekt Javowy na podstawie jednego slowa (potrzebujemy do tego konstruktora), pole mozemy wyciagnac jedno (select pole from tablica)
    // 2. z uzyciem Query - select word from words where id = ?
    // 3. da sie stworzyc interfejs o dowolnej nazwie, musi zawierac metody getCos() w srodku z nazwami odpowiadajacymi polom np. getWord(), getPronunciation()
    // takiego interfejsu uzywasz jako wartosc do zwrocenia
    // List<TwojInterfejs> findByCos()
}
