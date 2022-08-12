package com.example.demo.model.xml;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Cit {

    private String quote;

    @Getter(AccessLevel.NONE)
    private String type;

    @XmlAttribute
    public String getType() {
        return type;
    } /// String type czy Type type?
}

