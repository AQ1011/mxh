package com.example.socialapp.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

public class Post {
    private String id;
    private String userId;
    private String name;
    private String content;
    private ArrayList<String> imageUrl;
    private String dateAdded;

    public Post (){
        dateAdded = Calendar.getInstance().getTime().toString();
        content ="";
        imageUrl = new ArrayList<String>();
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setImageUrl(String... imageUrl){
        for(String url : imageUrl){
            this.imageUrl.add(url);
        }
    }

    public void setId (String id) {
        this.id = id;
    }
    public void setUserId (String id) {
        this.userId = id;
    }

    public String getPostId(){
        return id;
    }
    public String getImageUrl() {
        if(imageUrl.isEmpty())
            return "";
        return imageUrl.get(0);
    }
    public String getUserId() {
        return userId;
    }
    public String getContent() { return content; }
    @NonNull
    @Override
    public String toString() {
        return content;
    }
}
