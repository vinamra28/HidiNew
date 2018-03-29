package com.example.hp.hidi2;

/**
 * Created by HP on 15-Mar-18.
 */

public class CommentGet
{
    public String user_dp,user_name,user_comment,reply_commnet;

    public CommentGet(String user_name,String user_comment)
    {
        this.user_name=user_name;
        this.user_comment=user_comment;
    }

    public CommentGet(String user_dp, String user_name, String user_comment) {
        this.user_dp = user_dp;
        this.user_name = user_name;
        this.user_comment = user_comment;
    }

    public CommentGet(String user_dp, String user_name, String user_comment, String reply_commnet)
    {
        this.user_dp = user_dp;
        this.user_name = user_name;
        this.user_comment = user_comment;
        this.reply_commnet = reply_commnet;
    }

    public String getUser_dp()
    {
        return user_dp;
    }

    public void setUser_dp(String user_dp)
    {
        this.user_dp = user_dp;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public String getUser_comment()
    {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getReply_commnet() {
        return reply_commnet;
    }

    public void setReply_commnet(String reply_commnet) {
        this.reply_commnet = reply_commnet;
    }
}
