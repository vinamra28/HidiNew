package com.example.hp.hidi2;

/**
 * Created by asus on 28-03-2018.
 */

public class ChatListSet {
    private String url;
    private String name;
    private String uid;

    public ChatListSet(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public ChatListSet(String url, String name, String uid) {
        this.url = url;
        this.name = name;
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
