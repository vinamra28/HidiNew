package com.example.hp.hidi2;

/**
 * Created by asus on 31-03-2018.
 */

public class ChatHistorySet {
    public String name,uid,profileurl,lastSender,lastmsg,lasttime;

    public ChatHistorySet(String name, String uid, String profileurl) {
        this.name = name;
        this.uid = uid;
        this.profileurl = profileurl;
    }

    public ChatHistorySet(String name, String profileurl) {
        this.name = name;
        this.profileurl = profileurl;
    }

    public ChatHistorySet(String name, String profileurl, String lastSender, String lastmsg, String lasttime) {
        this.name = name;
        this.profileurl = profileurl;
        this.lastSender = lastSender;
        this.lastmsg = lastmsg;
        this.lasttime = lasttime;
    }

    public ChatHistorySet(String name, String uid, String profileurl, String lastSender, String lastmsg, String lasttime) {
        this.name = name;
        this.uid = uid;
        this.profileurl = profileurl;
        this.lastSender = lastSender;
        this.lastmsg = lastmsg;
        this.lasttime = lasttime;
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

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }
}
