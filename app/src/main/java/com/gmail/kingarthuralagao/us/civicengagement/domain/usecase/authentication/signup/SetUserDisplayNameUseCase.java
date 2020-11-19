package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignUpRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetUserDisplayNameUseCase extends BaseUseCase<FirebaseUser, SetUserDisplayNameUseCase.Params> {

    private SignUpRepository signUpRepository;

    public SetUserDisplayNameUseCase(SignUpRepository signUpRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signUpRepository = signUpRepository;
    }

    @Override
    protected Observable<FirebaseUser> createObservableUseCase(Params params) {
        return signUpRepository.setUserDisplayName(params.user, params.name);
    }

    public static final class Params {

        private final String name;
        private final FirebaseUser user;

        private Params(String name, FirebaseUser user) {
            this.name = name;
            this.user = user;
        }

        public static Params setUserDisplayName(String name, FirebaseUser user) {
            return new Params(name, user);
        }
    }
}
