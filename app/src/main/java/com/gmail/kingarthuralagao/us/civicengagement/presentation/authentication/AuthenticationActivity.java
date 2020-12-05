package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin.SignInFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup.SignUpFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeActivity;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityAuthenticationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class AuthenticationActivity extends AppCompatActivity
        implements IAuthenticationEventsListener {

    private ActivityAuthenticationBinding binding;
    private final String SIGNIN = "signin";
    private final String SIGNUP = "signup";
    private final String DIALOG = "dialog";
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private LoadingDialog loadingDialog;
    private Intent i;

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
        fetchUserDocument();
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

    public void fetchUserDocument() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        firestore
                .collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    CivicEngagementApp.setUser(task.getResult().toObject(User.class));
                    loadingDialog.dismiss();
                    i = new Intent(this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .addOnFailureListener(error -> {
                    loadingDialog.dismiss();
                    Toasty.error(this, "Error fetching user information.", Toasty.LENGTH_SHORT, true);
                });
    }
}