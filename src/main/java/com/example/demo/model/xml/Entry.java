package com.example.demo.model.xml;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Entry {
    private Form form;
    private GramGrp gramGrp;

    @Getter(AccessLevel.NONE)
    private Sense[] senses;

    @XmlElement(name = "sense")
    public Sense[] getSenses() {
        return senses;
    }
}
