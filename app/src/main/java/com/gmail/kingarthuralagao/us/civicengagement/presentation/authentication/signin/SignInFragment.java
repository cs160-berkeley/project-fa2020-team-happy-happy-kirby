package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.IAuthenticationEventsListener;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentSignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import es.dmoral.toasty.Toasty;


public class SignInFragment extends Fragment {


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    private static final int GOOGLE_SIGN_IN = 200;
    private final String TAG = getClass().getSimpleName();
    private FragmentSignInBinding binding;
    private FirebaseAuth firebaseAuth;
    private IAuthenticationEventsListener iAuthenticationEventsListener;
    private Boolean emailLayoutHasFocus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignInBinding.inflate(getLayoutInflater());
        firebaseAuth = ((CivicEngagementApp) requireActivity().getApplication()).getAuthInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpEvents();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAuthenticationEventsListener) {
            iAuthenticationEventsListener = (IAuthenticationEventsListener) context;
        } else {
            throw new RuntimeException("Must implement ISignInListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iAuthenticationEventsListener = null; // Prevent Memory Leak
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void setUpEvents() {
        binding.signUpTv.setOnClickListener(signUpTv -> iAuthenticationEventsListener.onSwitchToSignUp());

        binding.googleSignInBtn.setOnClickListener(googleSignInBtn -> initializeGoogleSignIn());

        binding.twitterSignInBtn.setOnClickListener(twitterSignInBtn -> initializeTwitterSignIn());

        binding.signInBtn.setOnClickListener(signInBtn -> {
            String emailInput = binding.emailEt.getText().toString();
            String passwordInput = binding.passwordEt.getText().toString();

            if (!hasEmptyFields() && hasValidEmail(emailInput))
                signInUser(emailInput, passwordInput);
        });

        addTextWatchers();
    }

    private void addTextWatchers() {
        binding.passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0)
                    binding.passwordLayout.setError("");
            }
        });

        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0)
                    binding.emailLayout.setError("");
            }
        });
    }

    /******************************* Password-based SignIn **********************************/

    private void signInUser(String email, String password) {
        iAuthenticationEventsListener.onStartLoading();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        iAuthenticationEventsListener.onStopLoading();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        iAuthenticationEventsListener.onStopLoading();
                        Toasty.error(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT, true).show();
                        //updateUI(null);
                    }
                });
    }

    private boolean hasEmptyFields() {
        boolean hasEmptyField = false;

        if (binding.emailEt.getText().length() == 0) {
            hasEmptyField = true;
            binding.emailLayout.setError("Email cannot be empty");
        }

        if (binding.passwordEt.getText().length() == 0) {
            hasEmptyField = true;
            binding.passwordLayout.setError("Password cannot be empty");
        }
        return hasEmptyField;
    }

    private boolean hasValidEmail(String email) {
        boolean hasValidEmail = true;

        if (!Utils.isValidEmail(email)) {
            hasValidEmail = false;
            binding.emailLayout.setError("Invalid Email");
        }

        return hasValidEmail;
    }

    /******************************* End of Password-based SignIn **********************************/
    
    /********************************** Google SignIn **********************************/
    private void initializeGoogleSignIn() {
        iAuthenticationEventsListener.onStartLoading();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            iAuthenticationEventsListener.onStopLoading();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode() + " " + e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        iAuthenticationEventsListener.onStopLoading();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        iAuthenticationEventsListener.onStopLoading();
                    }
                });
    }

    /********************************** End of Google SignIn **********************************/

    /********************************** Twitter SignIn **********************************/

    private void initializeTwitterSignIn() {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            finishSignIn(pendingResultTask); // There's something already here! Finish the sign-in for your user.
        } else {
            startSignInFlow(provider);
        }
    }

    private void startSignInFlow(OAuthProvider.Builder provider) {
        firebaseAuth
                .startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            Log.i(TAG, authResult.getAdditionalUserInfo().getProfile().toString());
                            // User is signed in.
                            // IdP data available in
                            // authResult.getAdditionalUserInfo().getProfile().
                            // The OAuth access token can also be retrieved:
                            // authResult.getCredential().getAccessToken().
                            // The OAuth secret can be retrieved by calling:
                            // authResult.getCredential().getSecret().
                            updateUI(authResult.getUser());
                        })
                .addOnFailureListener(
                        exception -> {
                            Toast.makeText(requireActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
    }

    private void finishSignIn(Task<AuthResult> pendingResultTask) {
        pendingResultTask
                .addOnSuccessListener(
                        authResult -> {
                            // User is signed in.
                            // IdP data available in
                            // authResult.getAdditionalUserInfo().getProfile().
                            // The OAuth access token can also be retrieved:
                            // authResult.getCredential().getAccessToken().
                            // The OAuth secret can be retrieved by calling:
                            // authResult.getCredential().getSecret().
                        })
                .addOnFailureListener(
                        exception -> {
                            // Handle failure.
                        });
    }
    
    /********************************** End of Google Twitter SignIn **********************************/

    private void updateUI(FirebaseUser user) {
        Log.i(TAG, "Name " + user.getDisplayName());
        iAuthenticationEventsListener.navigateToHome();
        //Toast.makeText(requireActivity(), "Name " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}