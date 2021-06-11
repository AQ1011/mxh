package com.example.socialapp.ui.main.userpost;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.Post;
import com.example.socialapp.data.model.User;

import java.util.ArrayList;

public class UserPostFragment extends Fragment implements UserPostAdapter.onPostListener {
    ImageView avatar;
    TextView username;
    String userId;
    UserPostAdapter userPostAdapter;
    RecyclerView recyclerView;
    User user;

    UserPostViewModel userPostViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        userId = UserPostFragmentArgs.fromBundle(getArguments()).getUserId();
        userPostViewModel = new ViewModelProvider(this, new UserPostViewModelFactory(userId))
                .get(UserPostViewModel.class);
        userPostViewModel.getPostsLiveData().observe(getViewLifecycleOwner(),postsUpdateObserver);
        userPostViewModel.getUserLiveData().observe(getViewLifecycleOwner(),userUpdateObserver);

        View root = inflater.inflate(R.layout.fragment_userposts, container, false);
        avatar = root.findViewById(R.id.iv_avatar);
        username = root.findViewById(R.id.tv_username);
        recyclerView = root.findViewById(R.id.rv_posts);

        return root;
    }

    Observer<ArrayList<Post>> postsUpdateObserver = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> posts) {
            userPostAdapter = new UserPostAdapter(posts, UserPostFragment.this::onPostClick);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(userPostAdapter);
        }
    };

    Observer<User> userUpdateObserver = new Observer<User>() {
        @Override
        public void onChanged(User u) {
            if(u!= null) {
                user = u;
                username.setText(u.getEmail());
                if(u.getAvatar()!= null) {
                    new UserPostAdapter.DownloadImageTask(avatar)
                            .execute(u.getAvatar().toString());
                }
            }
        }
    };

    @Override
    public void onPostClick(int position) {
        String postId = userPostAdapter.getPostId(position);
        NavDirections action = UserPostFragmentDirections.actionUserPostFragmentToPostFragment().setPostId(postId);
        Navigation.findNavController(getView()).navigate(action);
    }
}
