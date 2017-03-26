package com.intellchub.banke.login;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public interface LoginView {

    void showProgress();

    void showError();

    void showOffline();

    void setEmailError();

    void setPasswordError();

    void setUsernameError();

    void hideProgress();

    void hideError();

    void hideOffline();

    void navigateToHome();
}
