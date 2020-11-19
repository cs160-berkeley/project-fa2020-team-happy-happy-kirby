package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;

public interface SignInRepository {

    Observable<FirebaseUser> signInWithGoogle(String idToken);

    Observable<FirebaseUser> signInWithEmailAndPassword(String email, String password);
}
