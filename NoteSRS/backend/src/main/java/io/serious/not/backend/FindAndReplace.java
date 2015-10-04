package io.serious.not.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by thomas on 10/3/15.
 */
public class FindAndReplace {
    public FindAndReplace(){}

    private List<List<String>> textMatrix = new ArrayList<List<String>>();
    private List<String> punctuationList = new ArrayList<>();

    public List<List<String>> parseToTextMatrix(String text){
        for(int i = 0; i <= text.length() - 1; i++){
            if(text.charAt(i) == '.'){
                punctuationList.add(String.valueOf(text.charAt(i)));
            } else if(text.charAt(i) == '!'){
                punctuationList.add(String.valueOf(text.charAt(i)));
            } else if(text.charAt(i) == '?'){
                punctuationList.add(String.valueOf(text.charAt(i)));
            }
        }
        String[] textSentence = text.split("(?<=[.!?])\\s*"); //Splits text into sentences
        for (int i = 0; i <= textSentence.length - 1; i++) {
            textMatrix.add(Arrays.asList(textSentence[i].toLowerCase().replaceAll("[^a-z ]", "").split(" ")));
        }
        return textMatrix;
    }

    public String correctTextMatrix(List<List<String>> uncorrectedTextMatrix){
        String correctedParagraph = "";
        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        for (int i = 0; i <= uncorrectedTextMatrix.size() - 1; i++) { // for number of sentences
            for (int j = 0; j <= uncorrectedTextMatrix.get(i).size() - 1; j++) { // for number of words per sentence

                correctedParagraph += " ";

                Query.Filter exactWordParentFilter = new Query.FilterPredicate(
                        "originalWord",
                        Query.FilterOperator.EQUAL,
                        uncorrectedTextMatrix.get(i).get(j).toLowerCase()
                );
                Query qForReplacementList = new Query("ReplacementWord").setFilter(exactWordParentFilter);
                PreparedQuery pqForReplacementList = datastore.prepare(qForReplacementList);
                List<Entity> replacementList = pqForReplacementList.asList(FetchOptions.Builder.withDefaults());
                if (replacementList.size() > 0){
                    int rand = randInt(0, replacementList.size() - 1);
                    if (replacementList.get(rand).getKey().getName().equals("i")) {
                        correctedParagraph += "I";
                    }else if(j == 0  && replacementList.get(rand).getKey().getName().length() >= 1){
                        String replacementWordCap = replacementList.get(rand).getKey().getName();
                        correctedParagraph += Character.toUpperCase(replacementWordCap.charAt(0)) + replacementWordCap.substring(1);
                    } else {
                        correctedParagraph += replacementList.get(rand).getKey().getName();
                    }


                } else {
                    if (uncorrectedTextMatrix.get(i).get(j).equals("i")) {
                        correctedParagraph += "I";
                    }else if(j == 0 && uncorrectedTextMatrix.get(i).get(j).length() >= 1){
                        String replacementWordCap = uncorrectedTextMatrix.get(i).get(j);
                        correctedParagraph += Character.toUpperCase(replacementWordCap.charAt(0)) + replacementWordCap.substring(1);
                    } else{
                        correctedParagraph += uncorrectedTextMatrix.get(i).get(j);
                    }
                }
            }
            correctedParagraph += punctuationList.get(i);
        }
        if(correctedParagraph.length() > 0){
            return correctedParagraph.substring(1);
        } else {
            return "";
        }

    }

    private static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}