package com.example.socialapp.crud;

public class CommentActions {
    private static CommentActions commentActions;

    public static CommentActions getInstance(){
        if(commentActions == null){
            commentActions = new CommentActions();
        }
        return commentActions;
    }
    //bool ?
    public void postComment(){
        //TODO: post comment bruh
    }

    public void deleteComment() {
        //TODO: delete comment = set comment's userId = deletedUserId, content = ""
        //userid == deleted user id?
        //deleted user = ?
    }

}
