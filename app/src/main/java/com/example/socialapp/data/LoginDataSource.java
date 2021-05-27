package com.example.socialapp.data;

import com.example.socialapp.data.model.LoggedInUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LoggedInUser user;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            user = new LoggedInUser("","");

            synchronized (user) {
                user = new LoggedInUser("","");
                Task<AuthResult> loginTask = mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user =
                                new LoggedInUser(
                                        mAuth.getCurrentUser().getUid(),
                                        username);
                    }
                });
            }
            synchronized (user) {
                return new Result.Success<>(user);
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