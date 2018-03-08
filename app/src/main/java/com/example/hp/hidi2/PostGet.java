package com.example.hp.hidi2;

import android.graphics.drawable.Drawable;

/**
 * Created by HP on 06-Mar-18.
 */

public class PostGet {
    private String user_name, post_location, total_favours, do_arguments;
    private Drawable user_dp, user_post_image, do_like, do_dislike, image_argument;

    public PostGet() {
    }

    public PostGet(String user_name) {
        this.user_name = user_name;
    }

    public PostGet(String user_name, String post_location, String total_favours, String do_arguments) {
        this.user_name = user_name;
        this.post_location = post_location;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
    }

    public PostGet(String user_name, String post_location, String total_favours, Drawable user_dp, Drawable user_post_image) {
        this.user_name = user_name;
        this.post_location = post_location;
        this.total_favours = total_favours;
        this.user_dp = user_dp;
        this.user_post_image = user_post_image;
    }

    public PostGet(String user_name, String post_location, String total_favours, String do_arguments, Drawable user_dp, Drawable user_post_image) {
        this.user_name = user_name;
        this.post_location = post_location;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
        this.user_dp = user_dp;
        this.user_post_image = user_post_image;
    }

    public PostGet(String user_name, String post_location, String total_favours, String do_arguments, Drawable user_dp, Drawable user_post_image, Drawable do_like, Drawable do_dislike) {
        this.user_name = user_name;
        this.post_location = post_location;
        this.total_favours = total_favours;
        this.do_arguments = do_arguments;
        this.user_dp = user_dp;
        this.user_post_image = user_post_image;
        this.do_like = do_like;
        this.do_dislike = do_dislike;
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

    public String getTotal_favours() {
        return total_favours;
    }

    public void setTotal_favours(String total_favours) {
        this.total_favours = total_favours;
    }


    public Drawable getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(Drawable user_dp) {
        this.user_dp = user_dp;
    }

    public Drawable getUser_post_image() {
        return user_post_image;
    }

    public void setUser_post_image(Drawable user_post_image) {
        this.user_post_image = user_post_image;
    }

    public String getDo_arguments() {
        return do_arguments;
    }

    public void setDo_arguments(String do_arguments) {
        this.do_arguments = do_arguments;
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
