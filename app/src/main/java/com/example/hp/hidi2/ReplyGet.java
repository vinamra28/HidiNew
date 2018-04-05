package com.example.hp.hidi2;

/**
 * Created by HP on 05-Apr-18.
 */

public class ReplyGet
{
    String text;
    String sec_name;
    String profilepic;

    public ReplyGet(String text, String sec_name, String profilepic) {
        this.text = text;
        this.sec_name = sec_name;
        this.profilepic = profilepic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSec_name() {
        return sec_name;
    }

    public void setSec_name(String sec_name) {
        this.sec_name = sec_name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
