package com.example.socialapp.data.model;

import java.util.ArrayList;

public class ParentComment implements Comment{
    private String id;
    private String userId;
    private String postId;
    private String content;
    private String imageURL;
    ArrayList<Comment> child;

    public ArrayList<Comment> getChild() {
        return child;
    }

    public void setChild(ArrayList<Comment> child) {
        this.child = child;
    }

    public ArrayList<Comment> getChildren() {
        return child;
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

    public String getPostId() {
        return postId;
    }

    public void AddChild(ChildComment childComment) {
        child.add(childComment);
    }
}
