package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;

public interface SignUpRepository {

    Observable<Boolean> isEmailTaken(String email);

    Observable<FirebaseUser> signUpWithGoogle(String idToken);

    Observable<FirebaseUser> createUserWithEmailAndPassword(String email, String password);

    Observable<FirebaseUser> setUserDisplayName(FirebaseUser user, String name);
}
