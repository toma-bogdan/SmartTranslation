package com.company;

import java.util.ArrayList;


public class Word {
    String word;
    String word_en;
    String type;
    String[] singular;
    String[] plural;
    ArrayList<Definition> definitions;

    public Word(String word, String word_en, String type, String[] singular, String[] plural, ArrayList<Definition> definitions) {
        this.word = word;
        this.word_en = word_en;
        this.type = type;
        this.singular = singular;
        this.plural = plural;
        this.definitions = definitions;
    }

}
