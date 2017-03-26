package com.intellchub.banke.login;

import com.intellchub.banke.ChatterApiInterface;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private ChatterApiInterface chatterApiInterface;

    public LoginPresenterImpl(LoginView loginView, ChatterApiInterface chatterApiInterface) {
        this.loginView = loginView;
        this.chatterApiInterface = chatterApiInterface;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String email, String username, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginInteractor.login(email, password, username, this, chatterApiInterface);
    }


    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onEmailError() {

        if (loginView != null) {
            loginView.setEmailError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onUsernameError() {

    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }

    @Override
    public void onFailure() {

    }
}
