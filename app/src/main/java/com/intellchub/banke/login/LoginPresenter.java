package com.intellchub.banke.login;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public interface LoginPresenter {

    void validateCredentials(String email, String username, String password);

    void onDestroy();
}
