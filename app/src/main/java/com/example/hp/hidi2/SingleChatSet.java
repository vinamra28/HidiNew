package com.example.hp.hidi2;

/**
 * Created by asus on 06-04-2018.
 */

public class SingleChatSet {
    public String message;
    public String uid;

    public SingleChatSet(String message) {
        this.message = message;
    }

    public SingleChatSet(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
