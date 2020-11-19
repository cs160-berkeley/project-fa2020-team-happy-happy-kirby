package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignUpRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateUserWithEmailAndPasswordUseCase extends BaseUseCase<FirebaseUser, CreateUserWithEmailAndPasswordUseCase.Params> {

    private SignUpRepository signUpRepository;

    public CreateUserWithEmailAndPasswordUseCase(SignUpRepository signUpRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signUpRepository = signUpRepository;
    }

    @Override
    protected Observable<FirebaseUser> createObservableUseCase(Params params) {
        return signUpRepository.createUserWithEmailAndPassword(params.email, params.password);
    }

    public static final class Params {

        private final String email;
        private final String password;

        private Params(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static Params createUserWithEmailAndPassword(String email, String password) {
            return new Params(email, password);
        }
    }
}
