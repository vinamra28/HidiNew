package com.example.hp.hidi2;

/**
 * Created by asus on 05-04-2018.
 */

public class ChatWindowSet {
    public String uid;
    public String message;

    public ChatWindowSet(String message) {
        this.message = message;
    }

    public ChatWindowSet(String uid, String message) {
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
