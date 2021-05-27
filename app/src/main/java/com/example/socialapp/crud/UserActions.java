package com.example.socialapp.crud;

import com.example.socialapp.data.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

public class UserActions {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static UserActions userActions;

    public static UserActions getInstance () {
        if(userActions == null){
            userActions = new UserActions();
        }
        return userActions;
    }

    public void addUser (String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            com.example.socialapp.data.model.User u = new com.example.socialapp.data.model.User();
            u.setEmail(email);
            u.setUid(authResult.getUser().getUid());
            u.setDateAdded();
            firestore.collection("users").add(u);
        });
    }

    public void deleteUser (String uid){

    }

    public void updateUser (String uid){

    }

    public void updatePassword (User user, String password) {
        //TODO: implement update user password
    }

    public void signOut (){
        firebaseAuth.signOut();
    }

}
