package com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.core.Observable;

public class SignUpRepositoryImpl implements SignUpRepository {

    public static synchronized SignUpRepositoryImpl newInstance(Context c) {
        if (instance == null) {
            instance = new SignUpRepositoryImpl(c);
        }
        return instance;
    }

    private Context context;
    private static SignUpRepositoryImpl instance;

    public SignUpRepositoryImpl(Context c) {
        this.context = c;
    }

    private final String TAG = getClass().getSimpleName();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public Observable<Boolean> isEmailTaken(String email) {
        Observable<Boolean> observable = Observable.create(emitter -> {
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Log.e(TAG, "Is New User!");
                        } else {
                            Log.e(TAG, "Is Old User!");
                        }
                        emitter.onNext(!isNewUser);
                        emitter.onComplete();
                    });
        });
        return observable;
    }

    @Override
    public Observable<FirebaseUser> signUpWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        Observable<FirebaseUser> observable = Observable.create(emitter -> {
            FirebaseAuth.getInstance().signInWithCredential(credential)
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
}
