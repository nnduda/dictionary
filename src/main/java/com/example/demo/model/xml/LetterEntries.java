package com.example.demo.model.xml;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@XmlRootElement(name = "div")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LetterEntries implements Serializable {
    private static final long serialVersionUID = -5921117455863106823L;

    @Getter(AccessLevel.NONE)
    private Entry[] entries;

    @XmlElement(name = "entry")
    public Entry[] getEntries() {
        return entries;
    }
}
