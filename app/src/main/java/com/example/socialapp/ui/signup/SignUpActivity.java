package com.example.socialapp.ui.signup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.socialapp.Facade;
import com.example.socialapp.R;

public class SignUpActivity extends Activity {
    Facade facade = Facade.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText password2EditText = findViewById(R.id.password2);
        final Button signUpButton = findViewById(R.id.signup);
        signUpButton.setOnClickListener(v -> {
            if(Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches()
                && (passwordEditText.getText().toString().length() > 5)
                && passwordEditText.getText().toString().equals(password2EditText.getText().toString())){
                facade.signUpUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                finish();
            }
            else {
                Toast.makeText(this, "Sai rồi con trai", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
