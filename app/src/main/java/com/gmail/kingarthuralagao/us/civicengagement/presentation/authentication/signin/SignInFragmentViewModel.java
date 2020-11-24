package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signin.SignInRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignInRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signin.InitializeUserUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signin.SignInWithEmailAndPasswordUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signin.SignInWithGoogleUseCase;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import io.reactivex.rxjava3.observers.DisposableObserver;

public class SignInFragmentViewModel extends ViewModel {

    public StateLiveData<AdditionalUserInfo> googleSignInResponse = new StateLiveData<>();
    public StateLiveData<FirebaseUser> signInWithEmailAndPasswordResponse = new StateLiveData<>();
    public StateLiveData<Status> initializeUserResponse = new StateLiveData<>();


    private SignInWithGoogleUseCase signInWithGoogleUseCase =
            new SignInWithGoogleUseCase(SignInRepositoryImpl.newInstance());

    private SignInWithEmailAndPasswordUseCase signInWithEmailAndPasswordUseCase =
            new SignInWithEmailAndPasswordUseCase(SignInRepositoryImpl.newInstance());

    private InitializeUserUseCase initializeUserUseCase =
            new InitializeUserUseCase(SignInRepositoryImpl.newInstance());


    public void initializeSignInWithGoogle(String idToken) {
        googleSignInResponse.postLoading();

        DisposableObserver<AdditionalUserInfo> disposableObserver = new DisposableObserver<AdditionalUserInfo>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull AdditionalUserInfo additionalUserInfo) {
                googleSignInResponse.postSuccess(additionalUserInfo);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                googleSignInResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        signInWithGoogleUseCase.execute(disposableObserver, SignInWithGoogleUseCase.Params.signInWithGoogle(idToken));
    }

    public void setSignInWithEmailAndPasswordUseCase(String email, String password) {
        signInWithEmailAndPasswordResponse.postLoading();

        DisposableObserver<FirebaseUser> disposableObserver = new DisposableObserver<FirebaseUser>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull FirebaseUser firebaseUser) {
                signInWithEmailAndPasswordResponse.postSuccess(firebaseUser);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                signInWithEmailAndPasswordResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        signInWithEmailAndPasswordUseCase.execute(disposableObserver,
                SignInWithEmailAndPasswordUseCase.Params.signInWithEmailAndPassword(email, password));
    }

    public void initializeUser(FirebaseUser user, String name) {
        User newUser = new User(name, "", null, null, new ArrayList<>(), null, user.getUid());

        initializeUserResponse.postLoading();

        DisposableObserver<Status> disposableObserver = new DisposableObserver<Status>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Status status) {
                initializeUserResponse.postSuccess(status);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                initializeUserResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        initializeUserUseCase.execute(disposableObserver, InitializeUserUseCase.Params.initializeUser(user, newUser));
    }
}
