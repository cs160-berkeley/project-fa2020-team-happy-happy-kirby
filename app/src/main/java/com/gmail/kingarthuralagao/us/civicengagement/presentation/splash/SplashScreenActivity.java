package com.gmail.kingarthuralagao.us.civicengagement.presentation.splash;

import androidx.appcompat.app.AppCompatActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.login.LoginActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SplashScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}