package com.example.socialapp.ui.main.userpost;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserPostViewModelFactory implements ViewModelProvider.Factory{
    private String param;


    public UserPostViewModelFactory(String param) {
        this.param = param;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserPostViewModel(param);
    }
}
