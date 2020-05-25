package com.example.findmypet.Model;

import android.net.Uri;
import android.util.Log;

public class PostInfo {
    private String title, location , tel, description;
    private String image;
    private Uri imageUrl;
    private int position;
    private boolean permission;

    // constructor
    public PostInfo() {
    }

    public PostInfo(int position) {
        this.position = position;
    }

    public PostInfo(String title, String location, String tel, String image) {
        this.title = title;
        this.location = location;
        this.tel = tel;
        this.image = image;
    }

    // title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // location
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    // Tel no.
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    // Description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Image
    public String getImage() {
        Log.d("get Image" , "is "+ image);
        return image;
    }
    public void setImage(String image) {
        Log.d("set Image" , "is "+ image);
        this.image = image;

    }

    // setUri
    public Uri getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Permision
    public boolean isPermission() {
        return permission;
    }
    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}
