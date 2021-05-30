package com.example.socialapp.crud;

import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;

public class FirebaseCommentDecorator extends FirebaseDecorator {
    public FirebaseCommentDecorator(IFirebaseAction firebaseAction) {
        super(firebaseAction);
    }

    public void addChildToComment(String parentId, Comment child){

    }

}
