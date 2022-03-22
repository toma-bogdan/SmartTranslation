package com.company;

import java.io.File;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        try {
            File folder = new File("input");
            File[] file_list = folder.listFiles();
            Dictionary dictionary = new Dictionary();

            System.out.println("The words are being added to the dictionary ( there is only one duplicate)");
            if(file_list != null)
                dictionary.read_files(file_list);
            System.out.println();

            System.out.println("Removing one word that exists and one that doesn't:");
            System.out.println("The word jeu has been found and removed: " + dictionary.removeWord("jeu","fr"));
            System.out.println("The word doesn't exist has been found and removed: " + dictionary.removeWord("doesn't exist","ro") + "\n");


            System.out.println("Trying to add a definition that already exists to a word and one that doesn't exist:");
            Definition definition = new Definition("Dictionarul roman","sinonime",2002, new String[]{"a se plimba", "a umbla"});
            Definition definition1 = new Definition("Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)","definitions",2002,
                                                    new String[]{"a se deplasa, a umbla", "A putea fi înghițit ușor; a aluneca pe gât"});
            System.out.println("Definition has been added to the word: " + dictionary.addDefinitionForWord("merge","ro",definition));
            System.out.println("Definition has been added to the word: " + dictionary.addDefinitionForWord("merge","ro",definition1) + "\n");

            System.out.println("Trying to delete a definition from a word:");
            System.out.println("Definition has been removed from the word merge: " + dictionary.removeDefinition("merge","ro","Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)"));
            System.out.println("Definition has been removed from the word merge: " + dictionary.removeDefinition("merge","ro","Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)") + "\n");

            System.out.println("Translating words:");
            System.out.println("from fr to en:" + " manger = " + dictionary.translateWord("manger","fr","en"));
            System.out.println("from fr to ro: chat = " + dictionary.translateWord("chat","fr","ro") + "\n");

            System.out.println("Translate sentences:");
            System.out.println("Bogdan are câine. : " + dictionary.translateSentence("Bogdan are câine","ro","en"));
            System.out.println("pisica are păr. : " + dictionary.translateSentence("pisică are păr","ro","en") + "\n");

            System.out.println("Get definitions from a word:");
            ArrayList<Definition> definitions = dictionary.getDefinitionsForWord("chat","fr");
            ArrayList<Definition> definitions1 = dictionary.getDefinitionsForWord("merge","ro");
            System.out.println("chat: " + definitions);
            System.out.println("merge: " + definitions1 + "\n");


            System.out.println("Exporting ro dictionary");
            dictionary.exportDictionary("ro");
            System.out.println("Exporting fr dictionary");
            dictionary.exportDictionary("fr");

        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }
}

