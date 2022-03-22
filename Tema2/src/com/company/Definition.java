package com.company;

import java.util.Arrays;

public class Definition {
    String dict;
    String dictType;
    int year;
    String[] text;

    public Definition(String dict, String dictType, int year, String[] text) {
        this.dict = dict;
        this.dictType = dictType;
        this.year = year;
        this.text = text;
    }

    @Override
    public String toString() {
        return  "dict='" + dict + '\'' +
                ", dictType='" + dictType + '\'' +
                ", year=" + year +
                ", text=" + Arrays.toString(text) +
                '}';
    }
}
