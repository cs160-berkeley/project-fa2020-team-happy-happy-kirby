package com.gmail.kingarthuralagao.us.civicengagement;

import android.app.Application;
import android.content.Context;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CivicEngagementApp extends Application {

    private static FirebaseAuth mAuth;
    private static Context context;
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        this.context = getApplicationContext();
    }

    public static void fetchUserDocument() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore
                .collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    user = task.getResult().toObject(User.class);
                })
                .addOnFailureListener(error -> {

                });
    }

    public static void setUser(User u) {
        user = u;
    }
    public static User getUser() {
        return user;
    }

    public static Context getContext() {
        return context;
    }

    public FirebaseAuth getAuthInstance() {
        return mAuth;
    }
}
