package com.example.socialapp.data.model;

import android.net.Uri;

public class User {
    private String id;
    private String name;
    private Uri avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }
}
