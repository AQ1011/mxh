package com.example.socialapp;

import android.app.Activity;
import android.content.Intent;

import com.example.socialapp.crud.FirebaseAction;
import com.example.socialapp.crud.FirebaseCommentDecorator;
import com.example.socialapp.crud.FirebaseDecorator;
import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.data.model.User;
import com.example.socialapp.ui.login.LoginActivity;

public class Facade {
    FirebaseAction firebaseAction;

    private static Facade facade = new Facade();
    private Facade () {
        firebaseAction = FirebaseAction.getInstance();
    }

    public static Facade getInstance() {
        return facade;
    }

    public void addPost(Post post){
        firebaseAction.addPost(post);
    }

    public void addUserPost(String username, String password){
        firebaseAction.addUser(username,password);
    }

    public void addComment(Comment childComment){
        firebaseAction.addComment(childComment);
    }

    public void signUpUser(String username, String password) {
        firebaseAction.addUser(username,password);
    }

    public void signOut(Activity activity) {
        firebaseAction.signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void addChildToComment(String parentId, Comment child) {
        FirebaseCommentDecorator fb = new FirebaseCommentDecorator(firebaseAction);
        fb.addChildToComment(parentId, child);
    }

    public String getCurrentUserId() {
        return firebaseAction.getCurrentUser();
    }
}
