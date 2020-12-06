package com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication;

public interface IAuthenticationEventsListener {

    void onSwitchToSignIn();

    void onSwitchToSignUp();

    void onStartLoading(String txt);

    void onStopLoading();

    void navigateToHome();

    void onSetLoadingText(String txt);
}
