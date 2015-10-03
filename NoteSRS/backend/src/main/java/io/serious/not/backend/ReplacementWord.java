package io.serious.not.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by nick on 10/3/15.
 */
@Entity
public class ReplacementWord {
    private ReplacementWord(){}

    public ReplacementWord(String word){
        setWord(word);
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord(){
        return word;
    }

    public Ref<OriginalWord> getOriginalWordRef() {
        return originalWordRef;
    }

    public void setOriginalWordRef(OriginalWord originalWordRef) {
        originalWord = originalWordRef.getWord();
        this.originalWordRef = Ref.create(originalWordRef);
    }

    @Id
    @Index
    private String word; //Must be unique

    @Index
    private String originalWord;

    @Index
    private Ref<OriginalWord> originalWordRef;
}
