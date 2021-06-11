package com.example.socialapp.data.model;

import android.net.Uri;

import com.google.type.DateTime;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String uid;
    private String name;
    private String phone;
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    private Uri avatar;
    private String email;
    private Date dateAdded;
    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded() {
        this.dateAdded = Calendar.getInstance().getTime();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String id) {
        this.uid = id;
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
