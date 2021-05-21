package com.example.socialapp.crud;

import com.example.socialapp.data.model.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

public class UserActions {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private static UserActions userActions;

    public static UserActions getInstance () {
        if(userActions == null){
            userActions = new UserActions();
        }
        return userActions;
    }

    public void addUser (Post post){
        //TODO: implement add new (sign up)
    }

    public void deleteUser (String postId){

    }

    public void updateUser (String postId){

    }

    public void updatePassword (User user, String password) {
        //TODO: implement update user password
    }
}
