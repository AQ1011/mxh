package com.example.socialapp.crud;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialapp.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import io.grpc.Context;

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

    public void changeProfilePicture(Bitmap picture) {
        StorageReference storageRef = storage.getReference();
        StorageReference profilePicRef = storageRef.child("images/" + auth.getCurrentUser().getUid() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        firestore.collection("users")
                                .whereEqualTo("uid", auth.getCurrentUser().getUid()).get()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        String id = task1.getResult().getDocuments().get(0).getId();
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                            firestore.collection("users").document(id).update("avatar", uri.toString());
                                        });
                                    }
                                });
                    }
                });
            }
        });
    }
}
