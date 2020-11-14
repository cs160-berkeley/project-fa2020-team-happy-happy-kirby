package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {

    public interface ISignUpListener {
        void onSwitchToSignIn();
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    private FragmentSignUpBinding binding;
    private ISignUpListener iSignUpListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
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

    private void setListeners() {
        binding.signInTv.setOnClickListener(view -> iSignUpListener.onSwitchToSignIn());
    }
}