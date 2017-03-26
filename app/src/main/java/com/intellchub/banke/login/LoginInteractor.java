package com.intellchub.banke.login;

import com.intellchub.banke.ChatterApiInterface;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public interface LoginInteractor {

    interface OnLoginFinishedListener{

        void onEmailError();

        void onUsernameError();

        void onPasswordError();

        void onSuccess();

        void onFailure();

    }


    void login(String email, String username, String password, LoginInteractor.OnLoginFinishedListener onLoginFinishedListener, ChatterApiInterface chatterApiInterface);
}
