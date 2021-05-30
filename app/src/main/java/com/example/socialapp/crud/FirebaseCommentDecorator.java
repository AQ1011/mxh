package com.example.socialapp.crud;

import com.example.socialapp.data.model.ChildComment;

public class FirebaseCommentDecorator extends FirebaseDecorator {
    public FirebaseCommentDecorator(IFirebaseAction firebaseAction) {
        super(firebaseAction);
    }

    public void addChildToComment(String parentId, ChildComment child){

    }

}
