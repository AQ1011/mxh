package com.example.socialapp.data.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Post {
    private String postId;
    private String userId;
    private String name;
    private String content;
    private Long like;
    private ArrayList<String> imageUrl;

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    private Date dateAdded;

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public Post (){
        dateAdded = Calendar.getInstance().getTime();
        content ="";
        imageUrl = new ArrayList<String>();
        like = Long.valueOf(0);
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setImageUrl(String... imageUrl){
        for(String url : imageUrl){
            this.imageUrl.add(url);
        }
    }

    public void setPostId(String id) {
        this.postId = id;
    }
    public void setUserId (String id) {
        this.userId = id;
    }

    public String getPostId(){
        return postId;
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
