package com.example.socialapp.data.model;

public interface Comment {
    String getId();
    String getContent();
    String getUserId();
    String getPostId();
    void setId(String id);
    void setContent(String content);
    void setUserId(String userId);
    void setPostId(String postId);
}
