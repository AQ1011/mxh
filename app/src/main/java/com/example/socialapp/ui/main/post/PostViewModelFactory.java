package com.example.socialapp.ui.main.post;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.socialapp.data.LoginDataSource;
import com.example.socialapp.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class PostViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String param;


    public PostViewModelFactory(String param) {
        this.param = param;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PostViewModel(param);
    }
}