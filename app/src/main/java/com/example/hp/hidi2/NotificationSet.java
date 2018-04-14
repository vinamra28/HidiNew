package com.example.hp.hidi2;

/**
 * Created by asus on 13-04-2018.
 */

public class NotificationSet
{
    public String whatdid, whodid, timestamp;

    public NotificationSet(String whatdid, String whodid, String timestamp)
    {
        this.whatdid = whatdid;
        this.whodid = whodid;
        this.timestamp = timestamp;
    }

    public String getWhatdid() {
        return whatdid;
    }

    public void setWhatdid(String whatdid) {
        this.whatdid = whatdid;
    }

    public String getWhodid() {
        return whodid;
    }

    public void setWhodid(String whodid) {
        this.whodid = whodid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
