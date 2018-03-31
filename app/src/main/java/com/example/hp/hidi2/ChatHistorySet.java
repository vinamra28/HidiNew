package com.example.hp.hidi2;

/**
 * Created by asus on 31-03-2018.
 */

public class ChatHistorySet {
    public String name,uid,profileurl;

    public ChatHistorySet(String name, String uid, String profileurl) {
        this.name = name;
        this.uid = uid;
        this.profileurl = profileurl;
    }

    public ChatHistorySet(String name, String profileurl) {
        this.name = name;
        this.profileurl = profileurl;
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

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }
}
