package com.example.socialapp.crud;

import android.util.Log;

import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.ui.main.home.PostAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import static android.content.ContentValues.TAG;

public class FirebaseAction implements IFirebaseAction{
    private static FirebaseAction firebaseAction;

    public static FirebaseAction getInstance(){
        if(firebaseAction == null){
            firebaseAction = new FirebaseAction();
        }
        return firebaseAction;
    }

    public void addPost (Post post){
        firestore.collection("posts").add(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                firestore.collection("posts").document(task.getResult().getId())
                        .update("postId", task.getResult().getId());
            }
        });
    }

    public String getCurrentUser() {
        return auth.getCurrentUser().getUid();
    }


    public void addUser (String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
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
        auth.signOut();
    }

    public void addComment(Comment childComment){
        firestore.collection("comments").add(childComment).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String Id = task.getResult().getId();
                DocumentReference docref = task.getResult().getParent().document(Id);
                docref.update("id",Id);
            }
        });
    }

    public void deleteComment() {

    }
}
