package com.example.socialapp.data.model;

public class ChildComment implements Comment{
    private String id;
    private String userId;
    private String postId;
    private String content;

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getPostId() {
        return postId;
    }
}
