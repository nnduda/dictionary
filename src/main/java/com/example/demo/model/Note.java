package com.example.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ReferenceTableType referenceTable;

    private Long referenceTableId;

    private String note;

    public Note(String note) {
        this.note = note;
    }

    public Note(ReferenceTableType referenceTable, Long referenceTableId, String note) {
        this.referenceTable = referenceTable;
        this.referenceTableId = referenceTableId;
        this.note = note;
    }
}
