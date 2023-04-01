package com.example.demo.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        // {"abc", "abcd", "ab", "a" } -> "abc;abcd;ab;a"

        return strings != null ? String.join(";", strings) : "";

        /*String s = "";
        for(int i=0; i < strings.size(); i++){
            s += strings.get(i) + ";";
        }
        return s;*/
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return s != null ? Arrays.asList(s.split(";")) : new ArrayList<>();
    }
}
