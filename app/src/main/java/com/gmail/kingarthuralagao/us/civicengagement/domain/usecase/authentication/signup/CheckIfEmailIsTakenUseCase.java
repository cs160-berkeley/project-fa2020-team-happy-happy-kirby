package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository.authentication.signup.SignUpRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckIfEmailIsTakenUseCase extends BaseUseCase<Boolean, CheckIfEmailIsTakenUseCase.
        Params> {

    private SignUpRepository signUpRepository;

    public CheckIfEmailIsTakenUseCase(SignUpRepository signUpRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signUpRepository = signUpRepository;
    }

    @Override
    protected Observable<Boolean> createObservableUseCase(Params params) {
        return signUpRepository.isEmailTaken(params.email);
    }

    public static final class Params {

        private final String email;

        private Params(String email) {
            this.email = email;
        }

        public static Params forCheckEmail(String email) {
            return new Params(email);
        }
    }
}
