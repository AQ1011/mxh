package com.example.socialapp.crud;

import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseCommentDecorator extends FirebaseDecorator {
    public FirebaseCommentDecorator(IFirebaseAction firebaseAction) {
        super(firebaseAction);
    }

    public void addChildToComment(String parentId, Comment child){

        firestore.collection("comments").add(child).addOnCompleteListener(task -> {
            firestore.collection("comments").document(parentId).get().addOnCompleteListener(task2 -> {
                DocumentReference c = firestore.collection("comments").document(task.getResult().getId());
                ArrayList<DocumentReference> children = (ArrayList<DocumentReference>)task2.getResult().get("children");
                if(children == null)
                    children = new ArrayList<>();
                children.add(c);
                firestore.collection("comments").document(task.getResult().getId()).update("id",task.getResult().getId());
                firestore.collection("comments").document(parentId).update("children",children);
            });
        });
    }

}
