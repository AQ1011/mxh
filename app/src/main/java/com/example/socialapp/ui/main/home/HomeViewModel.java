package com.example.socialapp.ui.main.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialapp.data.model.Post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> postsLiveData;
    private ArrayList<Post> posts;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeViewModel() {
        postsLiveData = new MutableLiveData<>();
        init();
    }

    private void init() {
        posts = new ArrayList<Post>();
        db.collection("posts").orderBy("dateAdded").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            for (DocumentChange dc : value.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        QueryDocumentSnapshot document = dc.getDocument();
                            Post c = new Post();
                            c.setPostId(document.getId());
                            c.setContent(document.getString("content"));
                            c.setUserId(document.getString("userId"));
                            c.setImageUrl(document.getString("imageUrl"));
                            c.setLike(document.getLong("like"));
                            posts.add(0, c);
                        postsLiveData.setValue(posts);
                        break;
                    case MODIFIED:

                        break;
                    case REMOVED:
                        break;
                }
            }
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Log.d(TAG, document.getId() + " => " + document.getData());
//                    Post p = new Post();
//                    p.setId(document.getId());
//                    p.setContent(document.getString("content"));
//                    p.setUserId(document.getString("userId"));
//                    p.setImageUrl(document.get("imageUrl").toString());
//                    p.setLike(document.getLong("like"));
//                    posts.add(p);
//                }
//                postsLiveData.setValue(posts);
        });
    }

    public MutableLiveData<ArrayList<Post>> getPostsLiveData() {
        return postsLiveData;
    }
}