package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentSignInBinding;


public class SignInFragment extends Fragment {

    public interface ISignInListener {
        void onSwitchToSignUp();
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    private FragmentSignInBinding binding;
    private ISignInListener iSignInListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignInBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ISignInListener) {
            iSignInListener = (ISignInListener) context;
        } else {
            throw new RuntimeException("Must implement ISignInListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iSignInListener = null; // Prevent Memory Leak
    }

    private void setListeners() {
        binding.signUpTv.setOnClickListener(signUpTv -> iSignInListener.onSwitchToSignUp());
    }
}