package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signin;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignInRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignUpRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.SignUpWithGoogleUseCase;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignInWithGoogleUseCase extends BaseUseCase<AdditionalUserInfo, SignInWithGoogleUseCase.Params> {
    private SignInRepository signInRepository;

    public SignInWithGoogleUseCase(SignInRepository signInRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.signInRepository = signInRepository;
    }

    @Override
    protected Observable<AdditionalUserInfo> createObservableUseCase(Params params) {
        return signInRepository.signInWithGoogle(params.idToken);
    }

    public static final class Params {

        private final String idToken;

        private Params(String idToken) {
            this.idToken = idToken;
        }

        public static Params signInWithGoogle(String idToken) {
            return new SignInWithGoogleUseCase.Params(idToken);
        }
    }
}
