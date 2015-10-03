package io.serious.not.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thomas on 10/3/15.
 */
public class FindAndReplace {
    public FindAndReplace(){}

    private List<List<String>> parsedText = new ArrayList<List<String>>();

    private List<List<String>> parseText(String text){
        String[] textSentence = text.split("(?<=[.!?])\\s*"); //Splits text into sentences
        for (int i = 0; i <= textSentence.length-1; i++) {
            parsedText.add(Arrays.asList(textSentence[i].toLowerCase().replaceAll("[^a-z ]", "").split(" ")));
        }

        return parsedText;
    }

    private List<List<String>> getParsedText(){
        return parsedText;
    }
}