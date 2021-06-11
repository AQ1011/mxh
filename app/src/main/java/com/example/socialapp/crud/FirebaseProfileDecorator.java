package com.example.socialapp.crud;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialapp.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseProfileDecorator extends FirebaseDecorator {
    public FirebaseProfileDecorator(IFirebaseAction firebaseAction) {
        super(firebaseAction);
    }

    public Task<QuerySnapshot> getUser(String userId) {
        return firestore.collection("users").whereEqualTo("uid", userId).get();
    }

    public Task<QuerySnapshot> updateProfile(User user) {
        firestore.collection("users").whereEqualTo("uid",user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String id = task.getResult().getDocuments().get(0).getId();
                        //firestore.collection("users").document(id).set(user); // chỗ này nữa
                        DocumentReference m = firestore.collection("users").document(id);
                        m.update("phone", user.getPhone(), "name"
                                , user.getName(), "email", user.getEmail(), "address", user.getAddress());
                    }
                });
        return null;
    }
    public void changePassword(String email, String currPass, String newPass)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, currPass);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Log.d(TAG, "Password updated");
                                    } else {
                                        //Log.d(TAG, "Error password not updated")
                                    }
                                }
                            });
                        } else {
                            //Log.d(TAG, "Error auth failed")
                        }
                    }
                });
    }
}
