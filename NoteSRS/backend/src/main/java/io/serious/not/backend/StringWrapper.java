package io.serious.not.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class StringWrapper {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}