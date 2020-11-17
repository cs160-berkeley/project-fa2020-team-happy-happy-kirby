package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    @NonNull
    private final Application application;
    public SignUpFragmentViewModelFactory(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == SignUpFragmentViewModel.class) {
            return (T) new SignUpFragmentViewModel(application);
        }
        return null;
    }
}
