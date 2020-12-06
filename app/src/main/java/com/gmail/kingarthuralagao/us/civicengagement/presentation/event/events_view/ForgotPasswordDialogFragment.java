package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogFilterBinding;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getScreenWidth;

public class ForgotPasswordDialogFragment extends DialogFragment {
    public interface OnPasswordSend {
        void onPasswordSent();
    }

    public static FilterDialogFragment newInstance() {
        return new FilterDialogFragment();
    }

    private FragmentForgotPasswordBinding binding;
    private final String TAG = "ForgotPasswordDialog";
    OnPasswordSend onPasswordSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentForgotPasswordBinding.inflate(getLayoutInflater());
        if (getParentFragment() instanceof OnPasswordSend) {
            onPasswordSend = (OnPasswordSend) getParentFragment();
        }
        binding.forgotPasswordBtn.setOnClickListener(forgotPasswordBtn -> {
            String emailInput = binding.forgotPasswordEt.getText().toString();
            hideKeyboard(binding.forgotPasswordEt);
            if (hasValidEmail(emailInput)) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailInput)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                onPasswordSend.onPasswordSent();
                                dismiss();
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Email was not sent: " + e);
                    }
                });
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean hasValidEmail(String email) {
        boolean hasValidEmail = true;

        if (!Utils.isValidEmail(email)) {
            hasValidEmail = false;
            binding.forgotPasswordEt.setError("Invalid Email");
        }

        return hasValidEmail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = getScreenWidth();
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void setUpEvents() {
        binding.forgotPasswordCloseBtn.setOnClickListener(view -> dismiss());
    }

    private void setupViews() {
        //
    }
}
