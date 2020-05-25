package com.example.findmypet.Model;

import android.net.Uri;

public class Userlogin {
    private String name;
    private String email;
    private String userId;
    private Uri photoUri;

    // constructor
    public Userlogin() {
    }

    // name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // user id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // photo
    public Uri getPhotoUri() {
        return photoUri;
    }
    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}
