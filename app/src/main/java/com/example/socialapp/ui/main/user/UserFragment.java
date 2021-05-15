package com.example.socialapp.ui.main.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialapp.R;
import com.example.socialapp.data.LoginDataSource;
import com.example.socialapp.data.LoginRepository;
import com.example.socialapp.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    LoginRepository loginRepository;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_user, container, false);
        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final TextView name = root.findViewById(R.id.tv_user_frag_name);
        final TextView uid = root.findViewById(R.id.tv_user_frag_id);
        name.setText(user.getEmail());
        uid.setText(user.getUid());
        return root;
    }
}
