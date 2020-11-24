package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignUpRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InitializeUserUseCase extends BaseUseCase<Status, InitializeUserUseCase.Params> {

    private SignUpRepository signUpRepository;

    public InitializeUserUseCase(SignUpRepository signUpRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signUpRepository = signUpRepository;
    }

    @Override
    protected Observable<Status> createObservableUseCase(Params params) {
        return signUpRepository.initializeUser(params.user, params.newUser);
    }

    public static final class Params {

        private final FirebaseUser user;
        private final User newUser;

        private Params(FirebaseUser user, User newUser) {
            this.user = user;
            this.newUser = newUser;
        }

        public static Params initializeUser(FirebaseUser user, User newUser) {
            return new Params(user, newUser);
        }
    }
}
