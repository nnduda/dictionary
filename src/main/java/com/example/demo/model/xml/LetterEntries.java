package com.example.demo.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LetterEntries implements Serializable {
    private static final long serialVersionUID = -5921117455863106823L;
   // private Entry entry;
    private GramGrp gramGrp;
    private Form form;
    private Sense sense;
    private Sense sense2;

}