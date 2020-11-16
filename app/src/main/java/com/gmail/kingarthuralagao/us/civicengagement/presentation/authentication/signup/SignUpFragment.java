package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.Resource;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentSignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;


public class SignUpFragment extends Fragment {

    public interface ISignUpListener {
        void onSwitchToSignIn();
        void onStartLoading();
        void onStopLoading();
        void navigateToHome();
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    private final String TAG = getClass().getSimpleName();
    private final int GOOGLE_SIGN_IN = 200;
    private FragmentSignUpBinding binding;
    private ISignUpListener iSignUpListener;
    private FirebaseAuth firebaseAuth;
    private SignUpViewModel signUpViewModel;
    private Boolean emailLayoutHasFocus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        firebaseAuth = ((CivicEngagementApp) getActivity().getApplication()).getAuthInstance();
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        binding.nameLayout.setEndIconVisible(false);
        binding.emailLayout.setEndIconVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpEvents();
        subscribeToLiveData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ISignUpListener) {
            iSignUpListener = (ISignUpListener) context;
        } else {
            throw new RuntimeException("Must Implement ISignUpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iSignUpListener = null; // Prevent Memory Leak
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...)
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void subscribeToLiveData() {
        signUpViewModel.isEmailTakenResponse.observe(this, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> booleanResource) {
                switch (booleanResource.getStatus()) {
                    case LOADING:
                        iSignUpListener.onStartLoading();
                        break;
                    case SUCCESS:
                        Log.d(TAG, "Result: " + booleanResource.getData().toString());
                        iSignUpListener.onStopLoading();
                        if (booleanResource.getData() == true) {
                            binding.emailLayout.setErrorEnabled(true);
                            binding.emailLayout.setError("Email is already in use");
                        } else {
                            binding.emailLayout.setEndIconVisible(true);
                        }
                        break;
                    case ERROR:
                        Log.d(TAG, "Error" );
                        if (booleanResource.getError().getMessage() == "Invalid Email") {
                            binding.emailLayout.setErrorEnabled(true);
                            binding.emailLayout.setError("Invalid Email");
                        } else {
                            iSignUpListener.onStopLoading();
                        }
                        binding.emailLayout.setEndIconVisible(false);
                        break;
                    default:
                        Log.d(TAG, "Created");
                        break;
                }
            }
        });
    }

    private void setUpEvents() {
        binding.signInTv.setOnClickListener(view -> iSignUpListener.onSwitchToSignIn());

        binding.googleSignInBtn.setOnClickListener(view -> initializeGoogleSignIn());

        binding.twitterSignInBtn.setOnClickListener(view -> initializeTwitterSignIn());

        binding.signUpBtn.setOnClickListener(view -> {
            iSignUpListener.onStartLoading();
            createUser(binding.emailEt.getText().toString(), binding.passwordEt.getText().toString());
        });

        addTextWatchers();
    }

    private void addTextWatchers() {
        binding.nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.nameLayout.setEndIconVisible(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.nameLayout.setEndIconVisible(true);
                } else {
                    binding.nameLayout.setEndIconVisible(false);
                }
                setSignUpButtonStatus();
            }
        });

        binding.passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                setSignUpButtonStatus();
            }
        });

        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.emailLayout.setError("");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                setSignUpButtonStatus();
            }
        });

        binding.emailEt.setOnFocusChangeListener((view, hasFocus) -> {
            if (emailLayoutHasFocus && binding.emailEt.getText().length() != 0) {
                signUpViewModel.checkIfEmailTaken(binding.emailEt.getText().toString());
            }
            emailLayoutHasFocus = hasFocus;
        });
    }

    /******************************* Password-based SignUp **********************************/

    private void setSignUpButtonStatus() {
        if (hasEmptyFields() || !inputConditionsMet())
            disableSignUpButton();
        else
            enableSignUpButton();
    }

    private boolean hasEmptyFields() {
        return binding.nameEt.getText().length() == 0
                || binding.emailEt.getText().length() == 0
                || binding.passwordEt.getText().length() == 0;
    }

    private boolean inputConditionsMet() {
        return binding.passwordEt.getText().length() >= 6
                && (binding.emailLayout.getError() == null || binding.emailEt.getError().length() == 0);
    }

    private void enableSignUpButton() {
        binding.signUpBtn.setEnabled(true);
    }

    private void disableSignUpButton() {
        binding.signUpBtn.setEnabled(false);
    }

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        iSignUpListener.onStopLoading();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(requireActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        iSignUpListener.onStopLoading();
                        updateUI(null);
                    }
                });
    }

    /******************************* End of Password-based SignUp **********************************/

    /********************************** Google SignIn **********************************/

    private void initializeGoogleSignIn() {
        iSignUpListener.onStartLoading();
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
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
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
        iSignUpListener.onStopLoading();
        Log.i(TAG, "Name " + user.getDisplayName());
        iSignUpListener.navigateToHome();
        //Toast.makeText(requireActivity(), "Name " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}