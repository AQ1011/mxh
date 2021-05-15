package com.example.socialapp.data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialapp.R;
import com.example.socialapp.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LoggedInUser fakeUser;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            fakeUser = new LoggedInUser("","");
            // TODO: handle loggedInUser authentication

            synchronized (fakeUser) {
                fakeUser = new LoggedInUser("","");
                Task<AuthResult> loginTask = mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fakeUser =
                                new LoggedInUser(
                                        mAuth.getCurrentUser().getUid(),
                                        username);
                    }
                });
            }
            synchronized (fakeUser) {
                return new Result.Success<>(fakeUser);
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        mAuth.signOut();
    }
}