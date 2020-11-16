package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signup;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.InvalidEmailException;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup.SignUpRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.CheckIfEmailIsTakenUseCase;

import io.reactivex.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class SignUpViewModel extends ViewModel {

    public StateLiveData<Boolean> isEmailTakenResponse = new StateLiveData<>();
    public CheckIfEmailIsTakenUseCase checkIfEmailIsTakenUseCase = new CheckIfEmailIsTakenUseCase(new SignUpRepositoryImpl());

    @Override
    protected void onCleared() {
        super.onCleared();
        checkIfEmailIsTakenUseCase.dispose();
    }

    public void checkIfEmailTaken(String email) {
        if (Utils.isValidEmail(email)) {
            isEmailTakenResponse.postLoading();

            DisposableObserver<Boolean> disposableObserver = new DisposableObserver<Boolean>() {
                @Override
                public void onNext(@NonNull Boolean aBoolean) {
                    isEmailTakenResponse.postSuccess(aBoolean);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    isEmailTakenResponse.postError(e);
                }

                @Override
                public void onComplete() {}
            };

            checkIfEmailIsTakenUseCase.execute(disposableObserver, CheckIfEmailIsTakenUseCase.Params.forCheckEmail(email));
        } else {
            isEmailTakenResponse.postError(new InvalidEmailException("Invalid Email"));
        }
    }
}
