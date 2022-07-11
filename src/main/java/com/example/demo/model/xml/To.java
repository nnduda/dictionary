package com.example.demo.model.xml;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class To {

  //  @XmlElement()
  // private String value;

    @Getter(AccessLevel.NONE)
    private String attr;

    @XmlAttribute
    public String getAttr() {
        return attr;
    }
}
