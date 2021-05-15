package com.example.socialapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String uid;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String uid) {
        this.displayName = displayName;
        this.uid = uid;
    }

    String getDisplayName() {
        return displayName;
    }
    String getUid() {
        return uid;
    }
}