package com.gmail.kingarthuralagao.us.civicengagement;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class CivicEngagementApp extends Application {

    private FirebaseAuth mAuth;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        this.context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public FirebaseAuth getAuthInstance() {
        return mAuth;
    }
}
