package com.example.socialapp.crud;

import com.example.socialapp.data.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PostActions {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private static PostActions postActions;

    public static PostActions getInstance () {
        if(postActions == null){
            postActions = new PostActions();
        }
        return postActions;
    }

    public void addPost (Post post){
        firestore.collection("posts").add(post);
    }

    public void deletePost (String postId){

    }
}
