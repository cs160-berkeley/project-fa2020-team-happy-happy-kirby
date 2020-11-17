package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication;

public interface IAuthenticationEventsListener {

    void onSwitchToSignIn();

    void onSwitchToSignUp();

    void onStartLoading();

    void onStopLoading();

    void navigateToHome();
}
