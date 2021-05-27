package com.example.socialapp.ui.main.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Facade;
import com.example.socialapp.R;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.ui.main.home.HomeFragment;
import com.example.socialapp.ui.main.home.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    PostViewModel postViewModel;
    TextView username;
    TextView content;
    ImageView imageView;
    ImageView avatar;
    EditText ed_comment;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ImageButton ib_send;
    String postId;

    Facade facade = Facade.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        postViewModel = new ViewModelProvider(this, new PostViewModelFactory(postId))
                .get(PostViewModel.class);

        postViewModel.getPostLiveData().observe(getViewLifecycleOwner(),postUpdateObserver);
        postViewModel.getCommentsLiveData().observe(getViewLifecycleOwner(),commentsUpdateObserver);

        username = root.findViewById(R.id.tv_post_username);
        content = root.findViewById(R.id.tv_post_content);
        imageView = root.findViewById(R.id.iv_post_image);
        avatar = root.findViewById(R.id.iv_post_avatar);
        ed_comment = root.findViewById(R.id.ed_comment);
        ib_send = root.findViewById(R.id.ib_send_comment);
        ib_send.setOnClickListener(v -> sendComment(v));
        recyclerView = root.findViewById(R.id.rv_comments);
        recyclerView.setNestedScrollingEnabled(false);
        return root;
    }

    private void sendComment(View v) {
        if(ed_comment.getText().toString().trim().equals(""))
            return;
        Comment c = new Comment();
        c.setContent(ed_comment.getText().toString());
        c.setPostId(postId);
        c.setUserId(user.getUid());
        facade.addComment(c);
        ed_comment.setText("");
        ed_comment.clearFocus();
    }

    Observer<ArrayList<Comment>> commentsUpdateObserver = new Observer<ArrayList<Comment>>() {
        @Override
        public void onChanged(ArrayList<Comment> comments) {
            commentAdapter = new CommentAdapter(comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(commentAdapter);
        }
    };

    Observer<Post> postUpdateObserver = new Observer<Post>() {
        @Override
        public void onChanged(Post post) {
            getUserEmail(post.getUserId());
            content.setText(post.getContent());
            new PostAdapter.DownloadImageTask(imageView)
                    .execute(post.getImageUrl());
        }
    };

    private void getUserEmail(String uid){
        firestore.collection("users")
                .whereEqualTo("uid",uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    username.setText(document.getString("email"));
                }
            }
        });
    }
}
