package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignUpRepository;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpWithGoogleUseCase extends BaseUseCase<AdditionalUserInfo, SignUpWithGoogleUseCase.Params> {

    private SignUpRepository signUpRepository;

    public SignUpWithGoogleUseCase(SignUpRepository signUpRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signUpRepository = signUpRepository;
    }

    @Override
    protected Observable<AdditionalUserInfo> createObservableUseCase(Params params) {
        return signUpRepository.signUpWithGoogle(params.idToken);
    }

    public static final class Params {

        private final String idToken;

        private Params(String idToken) {
            this.idToken = idToken;
        }

        public static Params signUpWithGoogle(String idToken) {
            return new Params(idToken);
        }
    }
}
