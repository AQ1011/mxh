package com.example.socialapp.ui.main.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialapp.data.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Post p = new Post();
                                p.setId(document.getId());
                                p.setContent(document.getString("content"));
                                p.setUserId(document.getString("userId"));
                                p.setImageUrl(document.get("imageUrl").toString());
                                posts.add(p);
                            }
                            postsLiveData.setValue(posts);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public MutableLiveData<ArrayList<Post>> getPostsLiveData() {
        return postsLiveData;
    }
}