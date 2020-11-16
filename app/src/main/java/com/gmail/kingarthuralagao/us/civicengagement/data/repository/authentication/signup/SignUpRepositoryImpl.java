package com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.kingarthuralagao.us.civicengagement.domain.repository.authentication.signup.SignUpRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import javax.security.auth.Subject;

import io.reactivex.rxjava3.core.Observable;

public class SignUpRepositoryImpl implements SignUpRepository {

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
}
