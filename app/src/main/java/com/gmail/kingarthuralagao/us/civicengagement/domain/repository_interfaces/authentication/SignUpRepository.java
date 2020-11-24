package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;

public interface SignUpRepository {

    Observable<Boolean> isEmailTaken(String email);

    Observable<AdditionalUserInfo> signUpWithGoogle(String idToken);

    Observable<FirebaseUser> createUserWithEmailAndPassword(String email, String password);

    Observable<FirebaseUser> setUserDisplayName(FirebaseUser user, String name);

    Observable<Status> initializeUser(FirebaseUser user, User u);
}
