package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signin;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignInRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignInWithEmailAndPasswordUseCase extends BaseUseCase<FirebaseUser, SignInWithEmailAndPasswordUseCase.Params> {
    private SignInRepository signInRepository;

    public SignInWithEmailAndPasswordUseCase(SignInRepository signInRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signInRepository = signInRepository;
    }

    @Override
    protected Observable<FirebaseUser> createObservableUseCase(Params params) {
        return signInRepository.signInWithEmailAndPassword(params.email, params.password);
    }

    public static final class Params {

        private final String email;
        private final String password;

        private Params(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static Params signInWithEmailAndPassword(String email, String password) {
            return new SignInWithEmailAndPasswordUseCase.Params(email, password);
        }
    }
}
