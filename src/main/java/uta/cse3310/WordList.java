package uta.cse3310;

import java.util.HashMap; 
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader; 
import java.io.IOException; 


public class WordList { 
     private HashMap<String, Integer> words = new HashMap<String, Integer>(); 
     private ArrayList <String> wordList = new ArrayList<String>();  

     public WordList() {
          getWordsFromFile("words.txt");
          Collections.shuffle(wordList);
     }  
     public int getPoints(String word) { 
         return words.getOrDefault(word, 0); 
     }
     public void displayWordList() { 
          Collections.shuffle(wordList);
          for(int i = 0; i < wordList.size(); i++) {
              // System.out.println(wordList.get(i) + " Points for word : " + words.getOrDefault(wordList.get(i), 0));
          }
     } 
     public void getWordsFromFile(String fileName) {
        try { 
           
           BufferedReader fileReader = new BufferedReader(new FileReader(fileName)); 
           String buffer = null;  
           while((buffer = fileReader.readLine()) != null) {
               wordList.add(buffer);
               words.put(buffer, buffer.length());  
               
           } 
          fileReader.close();   
        }
        catch(IOException e) { 
            System.err.println("\n!!! Incorrect File Name !!! :" + e); 
            System.exit(-1); 
        }
     } 
     public ArrayList<String> getWordList() { 
          return wordList;

     }
}  
