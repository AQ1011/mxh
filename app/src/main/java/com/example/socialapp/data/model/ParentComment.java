package com.example.socialapp.data.model;

import java.util.ArrayList;

public class ParentComment implements Comment{
    private String id;
    private String userId;
    private String postId;
    private String content;
    private String imageURL;
    ArrayList<ChildComment> child;

    public ArrayList<ChildComment> getChild() {
        return child;
    }

    public void setChild(ArrayList<ChildComment> child) {
        this.child = child;
    }

    public ParentComment(String userId, String postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.child = new ArrayList<>();
    }
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPostId() {
        return postId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void AddChild(ChildComment childComment) {
        child.add(childComment);
    }
}
