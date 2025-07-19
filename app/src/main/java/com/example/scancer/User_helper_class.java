package com.example.scancer;

import android.graphics.Bitmap;

public class User_helper_class {
    String id_user;
    String user_name;
    String user_pass;
    String user_email;
    String img_prof;

    public User_helper_class(String id_user, String img_prof, String user_email, String user_pass, String user_name) {
        this.id_user = id_user;
        this.img_prof = img_prof;
        this.user_email = user_email;
        this.user_pass = user_pass;
        this.user_name = user_name;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getImg_prof() {
        return img_prof;
    }

    public void setImg_prof(String img_prof) {
        this.img_prof = img_prof;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }





}
