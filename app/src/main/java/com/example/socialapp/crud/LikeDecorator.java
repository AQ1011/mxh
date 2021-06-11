package com.example.socialapp.crud;

public class LikeDecorator extends FirebaseDecorator{
    public LikeDecorator(IFirebaseAction firebaseAction) {
        super(firebaseAction);
    }

    public void likePost(String postId){
        firestore.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Long like = task.getResult().getLong("like");
                firestore.collection("posts").document(postId).update("like",like+1);
            }
        });
    }
}
