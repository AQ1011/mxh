package com.example.socialapp;

import com.example.socialapp.crud.CommentActions;
import com.example.socialapp.crud.PostActions;
import com.example.socialapp.crud.UserActions;
import com.example.socialapp.data.model.Post;
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
}
