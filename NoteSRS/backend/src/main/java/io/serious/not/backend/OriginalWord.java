package io.serious.not.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by nick on 10/3/15.
 */
@Entity
public class OriginalWord {
    private OriginalWord(){}

    public OriginalWord(String word){
        setWord(word);
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord(){
        return word;
    }

    @Id
    @Index
    private String word; //Must be unique
}
