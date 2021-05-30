package com.example.socialapp.data.model;

public class ChildComment implements Comment{
    private String id;
    private String userId;
    private String postId;
    private String content;
    private String imageURL;

    @Override
    public String getPostId() {
        return null;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getUserId() {
        return postId;
    }
}
