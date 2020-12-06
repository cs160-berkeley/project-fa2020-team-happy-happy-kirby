package com.gmail.kingarthuralagao.us.civicengagement.presentation.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    static Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SplashScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (((CivicEngagementApp) getApplication()).getAuthInstance().getCurrentUser() != null) {
            fetchUserDocument();
        } else {
            i = new Intent(this, AuthenticationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void fetchUserDocument() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        firestore
                .collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    CivicEngagementApp.setUser(task.getResult().toObject(User.class));
                    i = new Intent(this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .addOnFailureListener(error -> {
                    Intent i = new Intent(this, AuthenticationActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(i);
                });
    }
}