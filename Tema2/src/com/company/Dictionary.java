package com.company;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Dictionary {
    //To keep all the words in a single collection, we use a Hashmap that uses language as key
    HashMap<String,ArrayList<Word>> dict = new HashMap<>();

    /*Method which opens all the files from a folder*/
    public void read_files(File[] file_list){

        try {
            for(File file : file_list) {
                //Extracts a country abbreviation (ex: ro, fr)
                String language = file.getName().substring(0,2);

                JsonParser jsonParser = new JsonParser();
                Object obj = jsonParser.parse(new FileReader(file));
                JsonArray jsonArray = (JsonArray) obj;

                //Goes through all the words in a json:
                for (Object o : jsonArray) {
                    Word word = parseWords((JsonObject) o);
                    System.out.println("The word has been added: " + addWord(word,language));
                }

            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    /*Creates a word from a json file*/
    public Word parseWords(JsonObject jsonObj){
        String word = jsonObj.get("word").toString().replace("\"" ,"");
        String word_en = jsonObj.get("word_en").toString().replace("\"","");
        String type = jsonObj.get("type").toString().replace("\"","");

        String sng = jsonObj.get("singular").toString().replace("\"","").replace("[","").replace("]","");
        String[] singular = sng.split(",");

        String plr = jsonObj.get("plural").toString().replace("\"","").replace("[","").replace("]","");
        String[] plural = plr.split(",");

        JsonArray definitions = jsonObj.getAsJsonArray("definitions");
        ArrayList<Definition> def = new ArrayList<>();

        for(Object o : definitions){
            parseDefinitions((JsonObject) o,def);
        }

        return new Word(word,word_en,type,singular,plural,def);
    }
    /*Creates all definitions assigned to a word from a json file */
    public void parseDefinitions(JsonObject jsonObj, ArrayList<Definition> def){
        String dict = jsonObj.get("dict").toString().replace("\"","");
        String dictType = jsonObj.get("dictType").toString().replace("\"","");
        String year = jsonObj.get("year").toString();

        String txt = jsonObj.get("text").toString();
        txt = txt.substring(0,txt.length() - 2);
        txt = txt.substring(2);
        String[] text = txt.split("\",\"");

        Definition definition = new Definition(dict,dictType,Integer.parseInt(year),text);
        def.add(definition);

    }

    public boolean addWord(Word word, String language){
        ArrayList<Word> words;
        /* Verifies if a language is already in the dictionary */
        if(dict.containsKey(language)){
            words = dict.get(language);
            /* Verifies if the word is already in the dictionary */
            for(Word w : words){
                if(w.word.equals(word.word)){
                    return false;
                }
            }
        }else {
            //if not, creates a new arraylist
            words = new ArrayList<>();
        }
        words.add(word);
        dict.put(language,words);
        return true;
    }
    public boolean removeWord(String word, String language){
        /* Verifies if the language exists in the dictionary */
        if(dict.containsKey(language)){
            ArrayList<Word> words = dict.get(language);
            for(Word w : words){
                /* Verifies if the word exists in the dictionary */
                if(w.word.equals(word)){
                    words.remove(w);
                    /*If there aren't any words from this language, it deletes the array*/
                    if(words.size() == 0){
                        dict.remove(language);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public boolean addDefinitionForWord(String word, String language, Definition definition){
        /* Verifies if the language exists in the dictionary */
        if(dict.containsKey(language)){
            ArrayList<Word> words = dict.get(language);
            for(Word w : words){
                /*Verifies if the word exists in the dictionary*/
                if(w.word.equals(word)){
                    /*Verifies if the definition already exists*/
                    for(Definition def : w.definitions){
                        if(def.dict.equals(definition.dict) && def.dictType.equals(definition.dictType)){
                            return false;
                        }
                    }
                    w.definitions.add(definition);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeDefinition(String word, String language, String dictionary){
        /* Verifies if the language exists in the dictionary */
        if(dict.containsKey(language)){
            ArrayList<Word> words = dict.get(language);
            for(Word w : words){
                /*Verifies if the word exists in the dictionary*/
                if(w.word.equals(word)){
                    for(Definition def : w.definitions){
                        /*Verifies if the has a definition from the given dictionary*/
                        if(def.dict.equals(dictionary)){
                            /*Removes the definition and returns true*/
                            w.definitions.remove(def);
                            return true;
                        }
                    }
                    /*The definition has not been found*/
                    return false;
                }
            }
        }
        return false;
    }
    public String translateWord(String word, String fromLanguage, String toLanguage){
        /* Verifies if the language exists in the dictionary */
        if(dict.containsKey(fromLanguage)){
            ArrayList<Word> words = dict.get(fromLanguage);
            /*Checks if the toLanguage is in the dictionary , so it translates it to english and then to "toLanguage"*/
            if(dict.containsKey(toLanguage)){
                String word_en = null;
                for(Word word1 : words){
                    if(word1.word.equals(word)){
                        word_en = word1.word_en;
                        break;
                    }
                }
                ArrayList<Word> wordArrayList = dict.get(toLanguage);
                for(Word word1 : wordArrayList){
                    if(word1.word_en.equals(word_en)){
                        return word1.word;
                    }
                }
            }else {/*If the toLanguage doesn't exist in the dictionary, it translates it to english*/
                for (Word w : words) {
                    if (w.word.equals(word)) {
                        return w.word_en;
                    }
                }
            }
        }
        return word;
    }

    public String translateSentence(String sentence, String fromLanguage, String toLanguage){
        String[] words = sentence.split(" ");
        String translatedSentence = null ;
        String translatedWord;
        for(String word : words){
            translatedWord = translateWord(word,fromLanguage,toLanguage);
            if(translatedSentence == null)
                translatedSentence = translatedWord;
            else
                translatedSentence = translatedSentence + " " + translateWord(word, fromLanguage, toLanguage);
        }
        return translatedSentence;
    }

    public ArrayList<Definition> getDefinitionsForWord(String word, String language){
        ArrayList<Word> words = dict.get(language);
        ArrayList<Definition> definitions = null;
        for(Word w : words){
            if(w.word.equals(word)){
                definitions = w.definitions;
                definitions.sort((Comparator.comparing(o -> o.year)));
            }
        }
        return definitions;
    }
    void exportDictionary(String language){

        ArrayList<Word> words = dict.get(language);
        words.sort(Comparator.comparing(o -> o.word));//sorts the words from a language

        for(Word word : words){
            word.definitions.sort(Comparator.comparing(o -> o.year));//sorts the definitions of every word
        }
        //Converting to json format:
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(words);

        //Writing in the output file:
        try {
            String filename;
            filename = "C:/Users/Bogdan/Desktop/Tema22/output/" + language + "_exportDict.json";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(json);
            bw.close();
        }catch (Exception e){
            System.out.println("Cannot export the data");
        }
    }
}
