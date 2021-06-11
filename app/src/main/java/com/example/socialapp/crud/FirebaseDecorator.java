package com.example.socialapp.crud;

public class FirebaseDecorator implements IFirebaseAction{
    private IFirebaseAction firebaseAction;

    public FirebaseDecorator(IFirebaseAction firebaseAction){
        this.firebaseAction = firebaseAction;
    }
}
