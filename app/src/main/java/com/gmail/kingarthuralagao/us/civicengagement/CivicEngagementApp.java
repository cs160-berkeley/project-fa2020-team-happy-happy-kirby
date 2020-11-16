package com.gmail.kingarthuralagao.us.civicengagement;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class CivicEngagementApp extends Application {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuthInstance() {
        return mAuth;
    }
}
