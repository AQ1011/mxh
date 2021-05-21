package com.example.socialapp.ui.main.post;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> postLiveData;
    private MutableLiveData<ArrayList<Comment>> postCommentsLiveData;
    private ArrayList<Comment> comments;
    private String postId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostViewModel(String postId) {
        postLiveData = new MutableLiveData<>();
        postCommentsLiveData = new MutableLiveData<>();
        comments = new ArrayList<>();
        this.postId = postId;
        init();
    }
    private void init() {
        DocumentReference docRef = db.collection("posts").document(postId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Post p = new Post();
                            p.setId(document.getId());
                            p.setContent(document.getString("content"));
                            p.setUserId(document.getString("userId"));
                            p.setImageUrl(document.get("imageUrl").toString());
                            postLiveData.setValue(p);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("postId").equals(postId)) {
                                    Comment c = new Comment();
                                    c.setId(document.getId());
                                    c.setContent(document.getString("content"));
                                    c.setUserId(document.getString("userId"));
                                    c.setPostId(document.getString("postId"));
                                    comments.add(c);
                                }
                            }
                            postCommentsLiveData.setValue(comments);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public MutableLiveData<Post> getPostLiveData() {
        return postLiveData;
    }
    public MutableLiveData<ArrayList<Comment>> getCommentsLiveData() {
        return postCommentsLiveData;
    }
}
