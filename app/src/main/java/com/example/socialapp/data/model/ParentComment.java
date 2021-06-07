package com.example.socialapp.data.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ParentComment implements Comment{
    private String id;
    private String userId;
    private String postId;
    private String content;
    ArrayList<DocumentReference> children;

    public ParentComment() {
        this.children = new ArrayList<>();
    }

    public void setChildren(ArrayList<DocumentReference> children) {
        this.children = children;
    }

    public ArrayList<DocumentReference> getChildren() {
        return children;
    }
    public ParentComment(String userId, String postId, String content, ArrayList<DocumentReference> children) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.children = new ArrayList<>();
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

    public void AddChild(DocumentReference childComment) {
        children.add(childComment);
    }
}
