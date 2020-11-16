package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = ((CivicEngagementApp) this.getApplication()).getAuthInstance();

        String userDisplayName = firebaseAuth.getCurrentUser().getDisplayName();
        binding.nameTv.setText("Hello "
                + (userDisplayName == null ? firebaseAuth.getCurrentUser().getEmail() : userDisplayName));
    }
}