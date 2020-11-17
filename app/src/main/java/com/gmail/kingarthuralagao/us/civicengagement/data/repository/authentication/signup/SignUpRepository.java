package com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;

public interface SignUpRepository {

    Observable<Boolean> isEmailTaken(String email);

    Observable<FirebaseUser> signUpWithGoogle(String idToken);
}
