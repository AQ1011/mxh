package com.example.socialapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.socialapp.crud.CommentActions;
import com.example.socialapp.crud.PostActions;
import com.example.socialapp.crud.UserActions;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.ui.login.LoginActivity;
import com.example.socialapp.ui.main.home.PostAdapter;

public class Facade {
    PostActions postActions;
    CommentActions commentActions;
    UserActions userActions;

    private static Facade facade = new Facade();
    private Facade () {
        postActions = PostActions.getInstance();
        commentActions = CommentActions.getInstance();
        userActions = UserActions.getInstance();
    }

    public static Facade getInstance() {
        return facade;
    }

    public void addPost(Post post){
        postActions.addPost(post);
    }

    public void addUserPost(String username, String password){
        userActions.addUser(username,password);
    }

    public void addComment(Comment comment){
        commentActions.addComment(comment);
    }

    public void signUpUser(String username, String password) {
        userActions.addUser(username,password);
    }
    public void signOut(Activity activity) {
        userActions.signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
