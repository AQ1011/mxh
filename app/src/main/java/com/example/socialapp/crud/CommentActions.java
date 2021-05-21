package com.example.socialapp.crud;

import com.example.socialapp.data.model.Comment;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentActions {
    private static CommentActions commentActions;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static CommentActions getInstance(){
        if(commentActions == null){
            commentActions = new CommentActions();
        }
        return commentActions;
    }

    public void addComment(Comment comment){
        firestore.collection("comments").add(comment);
    }

    public void deleteComment() {
        //TODO: delete comment = set comment's userId = deletedUserId, content = ""
        //userid == deleted user id?
        //deleted user = ?
    }

}
