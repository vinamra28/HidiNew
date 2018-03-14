package com.example.hp.hidi2;

import android.graphics.drawable.Drawable;

/**
 * Created by HP on 06-Mar-18.
 */

public class PostGet
{
    private String pid,user_dp, user_name , post_location , user_post_image , total_favours , do_arguments , total_dislikes,like,dislike;
    private Drawable  do_like, do_dislike, image_argument;

    public PostGet(String user_name, String total_favours, String do_arguments, String user_dp, String user_post_image)
    {
        this.user_name = user_name;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
        this.user_dp = user_dp;
        this.user_post_image = user_post_image;
    }

    public PostGet(String pid, String user_dp, String user_name, String post_location, String user_post_image, String total_favours, String do_arguments, String total_dislikes, String like, String dislike)
    {
        this.pid = pid;
        this.user_dp = user_dp;
        this.user_name = user_name;
        this.post_location = post_location;
        this.user_post_image = user_post_image;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
        this.total_dislikes = total_dislikes;
        this.like = like;
        this.dislike = dislike;
    }

    public PostGet() {
    }

    public PostGet(String user_dp, String user_name, String post_location, String user_post_image, String total_favours, String do_arguments, String total_dislikes)
    {
        this.user_dp = user_dp;
        this.user_name = user_name;
        this.post_location = post_location;
        this.user_post_image = user_post_image;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
        this.total_dislikes = total_dislikes;
    }
//    public String getTotal_dislikes()
//    {
//        return total_dislikes;
//    }
//
//    public void setTotal_dislikes(String total_dislikes)
//    {
//        this.total_dislikes = total_dislikes;
//    }
//
//    public String getUser_name()
//    {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name)
//    {
//        this.user_name = user_name;
//    }
//
//    public String getPost_location()
//    {
//        return post_location;
//    }
//
//    public void setPost_location(String post_location)
//    {
//        this.post_location = post_location;
//    }
//
//    public String getTotal_favours()
//    {
//        return total_favours;
//    }
//    public void setTotal_favours(String total_favours)
//    {
//        this.total_favours = total_favours;
//    }
//    public String getUser_dp()
//    {
//        return user_dp;
//    }
//    public void setUser_dp(String user_dp)
//    {
//        this.user_dp = user_dp;
//    }
//    public String getUser_post_image()
//    {
//        return user_post_image;
//    }
//    public void setUser_post_image(String user_post_image)
//    {
//        this.user_post_image = user_post_image;
//    }
//    public String getDo_arguments()
//    {
//        return do_arguments;
//    }
//    public void setDo_arguments(String do_arguments)
//    {
//        this.do_arguments = do_arguments;
//    }
//    public Drawable getDo_like()
//    {
//        return do_like;
//    }
//    public void setDo_like(Drawable do_like)
//    {
//        this.do_like = do_like;
//    }
//    public Drawable getDo_dislike()
//    {
//        return do_dislike;
//    }
//    public void setDo_dislike(Drawable do_dislike)
//    {
//        this.do_dislike = do_dislike;
//    }
//    public Drawable getImage_argument()
//    {
//        return image_argument;
//    }
//    public void setImage_argument(Drawable image_argument)
//    {
//        this.image_argument = image_argument;
//    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPost_location() {
        return post_location;
    }

    public void setPost_location(String post_location) {
        this.post_location = post_location;
    }

    public String getUser_post_image() {
        return user_post_image;
    }

    public void setUser_post_image(String user_post_image) {
        this.user_post_image = user_post_image;
    }

    public String getTotal_favours() {
        return total_favours;
    }

    public void setTotal_favours(String total_favours) {
        this.total_favours = total_favours;
    }

    public String getDo_arguments() {
        return do_arguments;
    }

    public void setDo_arguments(String do_arguments) {
        this.do_arguments = do_arguments;
    }

    public String getTotal_dislikes() {
        return total_dislikes;
    }

    public void setTotal_dislikes(String total_dislikes) {
        this.total_dislikes = total_dislikes;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public Drawable getDo_like() {
        return do_like;
    }

    public void setDo_like(Drawable do_like) {
        this.do_like = do_like;
    }

    public Drawable getDo_dislike() {
        return do_dislike;
    }

    public void setDo_dislike(Drawable do_dislike) {
        this.do_dislike = do_dislike;
    }

    public Drawable getImage_argument() {
        return image_argument;
    }

    public void setImage_argument(Drawable image_argument) {
        this.image_argument = image_argument;
    }
}
