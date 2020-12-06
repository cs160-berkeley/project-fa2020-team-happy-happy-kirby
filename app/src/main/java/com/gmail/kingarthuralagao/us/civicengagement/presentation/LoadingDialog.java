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

import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogLoadingBinding;

public class LoadingDialog extends DialogFragment {

    private static DialogLoadingBinding binding;
    private final static String LOADING_TEXT = "loadingText";
    private String loadingTxt = "";

    public static LoadingDialog newInstance(String loadingText) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle args = new Bundle();
        args.putString(LOADING_TEXT, loadingText);
        loadingDialog.setArguments(args);
        return loadingDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogLoadingBinding.inflate(getLayoutInflater());

        loadingTxt = getArguments().getString(LOADING_TEXT);
        if (loadingTxt.length() == 0) {
            showCircularProgressBar();
        } else {
            binding.progressTv.setText(loadingTxt);
            binding.progressCircular.setVisibility(View.GONE);
        }
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
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public static void setLoadingText(String newLoadingTxt) {
        binding.progressTv.setText(newLoadingTxt);
    }

    public static void showCircularProgressBar() {
        binding.loadingTxtLay.setVisibility(View.GONE);
        binding.progressCircular.setVisibility(View.VISIBLE);
    }
}
