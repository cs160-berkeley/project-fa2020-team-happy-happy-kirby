package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin.SignInFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup.SignUpFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeActivity;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity
        implements SignInFragment.ISignInListener, SignUpFragment.ISignUpListener {

    private ActivityAuthenticationBinding binding;
    private final String SIGNIN = "signin";
    private final String SIGNUP = "signup";
    private final String DIALOG = "dialog";
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createFragments();
        setFrameToSignIn();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onSwitchToSignUp() {
        switchToSignUp();
    }

    @Override
    public void onSwitchToSignIn() {
        switchToSignIn();
    }

    @Override
    public void onStartLoading() {
        loadingDialog = new LoadingDialog();
        loadingDialog.show(getSupportFragmentManager(), DIALOG);
    }

    @Override
    public void onStopLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void navigateToHome() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    private void createFragments() {
        signInFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
    }

    private void setFrameToSignIn() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.authenticationFrameLayout.getId(), signInFragment, SIGNIN)
                .add(binding.authenticationFrameLayout.getId(), signUpFragment, SIGNUP)
                .hide(signUpFragment)
                .commit();
    }

    private void switchToSignIn() {
        getSupportFragmentManager()
                .beginTransaction()
                .show(signInFragment)
                .hide(signUpFragment)
                .commit();
    }

    private void switchToSignUp() {
        getSupportFragmentManager()
                .beginTransaction()
                .show(signUpFragment)
                .hide(signInFragment)
                .commit();
    }
}