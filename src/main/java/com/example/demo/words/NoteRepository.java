package com.example.demo.words;

import com.example.demo.model.Note;
import com.example.demo.model.ReferenceTableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(value = "SELECT n FROM Note u WHERE u.referenceTable = :tableType", nativeQuery = true)
    List<Note> findByReferenceTable(@Param("tableType") ReferenceTableType tableType);
}
