package com.example.hp.hidi2;

import android.graphics.drawable.Drawable;

/**
 * Created by asus on 19-04-2018.
 */

public class FinalSet {
    public String url,uid,name;

    public FinalSet(String url, String uid, String name) {
        this.url = url;
        this.uid = uid;
        this.name = name;
    }

    public FinalSet(String url, String uid, String name, Drawable check) {
        this.url = url;
        this.uid = uid;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
