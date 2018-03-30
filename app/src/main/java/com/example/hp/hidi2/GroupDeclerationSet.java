package com.example.hp.hidi2;

/**
 * Created by asus on 29-03-2018.
 */

public class GroupDeclerationSet {
    public String admin,groupImage,lastMessage,lastSender,lastUpdated,name,type;

    public GroupDeclerationSet(String admin, String groupImage, String lastMessage, String lastSender, String lastUpdated, String name, String type) {
        this.admin = admin;
        this.groupImage = groupImage;
        this.lastMessage = lastMessage;
        this.lastSender = lastSender;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.type = type;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
