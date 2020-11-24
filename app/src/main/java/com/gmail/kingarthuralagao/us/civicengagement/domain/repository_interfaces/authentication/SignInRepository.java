package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;

public interface SignInRepository {

    Observable<AdditionalUserInfo> signInWithGoogle(String idToken);

    Observable<FirebaseUser> signInWithEmailAndPassword(String email, String password);

    Observable<Status> initializeUser(FirebaseUser user, User newUser);
}
