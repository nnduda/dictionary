package com.example.demo.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Note implements Serializable {
    private static final long serialVersionUID = 1303334841506638337L;
    //private String to;
    private To to;
    private String from;
    private String heading;
    private String body;

}
