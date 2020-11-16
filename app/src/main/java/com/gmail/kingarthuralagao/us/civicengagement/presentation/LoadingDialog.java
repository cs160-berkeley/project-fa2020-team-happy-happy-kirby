package com.gmail.kingarthuralagao.us.civicengagement.presentation;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogLoadingBinding;

public class LoadingDialog extends DialogFragment {

    private DialogLoadingBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogLoadingBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set background to transparent
        Dialog dialog = getDialog();
        if (dialog != null) {
            ColorDrawable colorDrawable = new ColorDrawable(0xffffff);
            dialog.getWindow().setBackgroundDrawable(colorDrawable);
        }
    }
}
