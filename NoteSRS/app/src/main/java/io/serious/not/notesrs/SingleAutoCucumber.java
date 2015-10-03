package io.serious.not.notesrs;

import android.content.Context;

/**
 * Created by nick on 10/3/15.
 */
public class SingleAutoCucumber {
    private String toBeCorrected;
    private String correction;
    private Context context;

    public SingleAutoCucumber(){}

    public SingleAutoCucumber(String toBeCorrected, String correction, Context context){
        this.setToBeCorrected(toBeCorrected);
        this.setCorrection(correction);
        this.setContext(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getToBeCorrected() {
        return toBeCorrected;
    }

    public void setToBeCorrected(String toBeCorrected) {
        this.toBeCorrected = toBeCorrected;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

}
