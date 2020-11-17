package com.gmail.kingarthuralagao.us.civicengagement.domain.repository.authentication.signup;


import io.reactivex.rxjava3.core.Observable;

public interface SignUpRepository {

    Observable<Boolean> isEmailTaken(String email);
}
