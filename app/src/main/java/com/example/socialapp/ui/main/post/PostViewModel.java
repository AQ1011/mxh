package com.example.socialapp.ui.main.post;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.data.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        DocumentReference postRef = db.collection("posts").document(postId);
        postRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Post p = new Post();
                            p.setPostId(document.getId());
                            p.setContent(document.getString("content"));
                            p.setUserId(document.getString("userId"));
                            p.setImageUrl(document.getString("imageUrl"));
                            p.setLike(document.getLong("like"));
                            postLiveData.setValue(p);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
//        db.collection("comments").whereEqualTo("postId", postId).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Comment c = new Comment();
//                                    c.setId(document.getId());
//                                    c.setContent(document.getString("content"));
//                                    c.setUserId(document.getString("userId"));
//                                    c.setPostId(document.getString("postId"));
//                                    comments.add(c);
//                            }
//                            postCommentsLiveData.setValue(comments);
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
        db.collection("comments").whereEqualTo("postId", postId).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            for (DocumentChange dc : value.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        QueryDocumentSnapshot document = dc.getDocument();
                        if(document.get("children") == null) {
                            Comment c = new ChildComment();
                            c.setId(document.getId());
                            c.setContent(document.getString("content"));
                            c.setUserId(document.getString("userId"));
                            c.setPostId(document.getString("postId"));
                            comments.add(0, c);
                        }
                        else {
                            ParentComment c = new ParentComment();
                            c.setId(document.getId());
                            c.setContent(document.getString("content"));
                            c.setUserId(document.getString("userId"));
                            c.setPostId(document.getString("postId"));
                            ArrayList<DocumentReference> children = (ArrayList<DocumentReference>)(document.get("children"));
                            c.setChildren(children);
                            comments.add(0, c);
                        }
                        postCommentsLiveData.setValue(comments);
                        break;
                    case MODIFIED:
                        break;
                    case REMOVED:
                        break;
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
