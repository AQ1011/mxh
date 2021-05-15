package com.example.socialapp.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.socialapp.data.LoginDataSource;
import com.example.socialapp.data.LoginRepository;
import com.example.socialapp.data.Result;
import com.example.socialapp.data.model.LoggedInUser;
import com.example.socialapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private FirebaseAuth mAuth;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        mAuth = FirebaseAuth.getInstance();
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task ->  {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(),data.getUserId())));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                            }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthException) {
                        ((FirebaseAuthException) e).getErrorCode();
                    }
                }
            });
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
//            return !username.trim().isEmpty();
//        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}