package com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup.SignUpRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.authentication.SignInRepository;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.core.Observable;

public class SignInRepositoryImpl implements SignInRepository {

    public static synchronized SignInRepositoryImpl newInstance() {
        if (instance == null) {
            instance = new SignInRepositoryImpl();
        }
        return instance;
    }

    private static SignInRepositoryImpl instance;

    @Override
    public Observable<AdditionalUserInfo> signInWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        Observable<AdditionalUserInfo> observable = Observable.create(emitter ->
                FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        emitter.onNext(task.getResult().getAdditionalUserInfo());
                    } else {
                        emitter.onError(task.getException());
                    }
                    emitter.onComplete();
                }));
        return observable;
    }

    @Override
    public Observable<FirebaseUser> signInWithEmailAndPassword(String email, String password) {
        Observable<FirebaseUser> observable = Observable.create(emitter -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onNext(task.getResult().getUser());
                        } else {
                            emitter.onError(task.getException());
                        }
                        emitter.onComplete();
                    });
        });
        return observable;
    }

    @Override
    public Observable<Status> initializeUser(FirebaseUser user, User newUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Observable<Status> observable = Observable.create(emitter ->
                db.collection("Users").document(user.getUid())
                        .set(newUser)
                        .addOnSuccessListener(aVoid -> {
                            emitter.onNext(Status.SUCCESS);
                            emitter.onComplete();
                            Log.d(getClass().getSimpleName(), "DocumentSnapshot successfully written!");
                        })
                        .addOnFailureListener(e -> {
                            emitter.onError(e);
                            emitter.onComplete();
                            Log.w(getClass().getSimpleName(), "Error writing document", e);
                        }));

        return observable;
    }
}
