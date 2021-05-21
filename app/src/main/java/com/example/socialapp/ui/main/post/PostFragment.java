package com.example.socialapp.ui.main.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.ui.main.home.HomeFragment;
import com.example.socialapp.ui.main.home.PostAdapter;

import java.util.ArrayList;

public class PostFragment extends Fragment {
    PostViewModel postViewModel;
    TextView username;
    TextView content;
    ImageView imageView;
    ImageView avatar;

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        postViewModel = new ViewModelProvider(this, new PostViewModelFactory(postId))
                .get(PostViewModel.class);

        postViewModel.getPostLiveData().observe(getViewLifecycleOwner(),postUpdateObserver);
        postViewModel.getCommentsLiveData().observe(getViewLifecycleOwner(),commentsUpdateObserver);

        username = root.findViewById(R.id.tv_post_username);
        content = root.findViewById(R.id.tv_post_content);
        imageView = root.findViewById(R.id.iv_post_image);
        avatar = root.findViewById(R.id.iv_post_avatar);

        recyclerView = root.findViewById(R.id.rv_comments);

        return root;
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
            username.setText(post.getUserId());
            content.setText(post.getContent());
            new PostAdapter.DownloadImageTask(imageView)
                    .execute(post.getImageUrl());
        }
    };
}
