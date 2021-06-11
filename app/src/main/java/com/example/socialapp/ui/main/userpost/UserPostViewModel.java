package com.example.socialapp.ui.main.userpost;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class UserPostViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> postsLiveData;
    private MutableLiveData<User> userLiveData;
    private ArrayList<Post> posts;
    private String userId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public UserPostViewModel(String userId) {
        postsLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        posts = new ArrayList<>();
        this.userId = userId;
        init();
    }

    private void init() {
        posts = new ArrayList<Post>();
        db.collection("users").whereEqualTo("uid",userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                User u = new User();
                u.setAvatar(Uri.parse(doc.getString("avatar")));
                u.setUid(doc.getString("uid"));
                u.setEmail(doc.getString("email"));
                userLiveData.setValue(u);
            }
        });

        db.collection("posts").whereEqualTo("userId",userId)
                .orderBy("dateAdded").addSnapshotListener((value, error) -> {
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
        });
    }

    public MutableLiveData<ArrayList<Post>> getPostsLiveData() {
        return postsLiveData;
    }
    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
