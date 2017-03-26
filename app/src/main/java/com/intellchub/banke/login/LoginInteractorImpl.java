package com.intellchub.banke.login;

import android.text.TextUtils;
import android.util.Log;

import com.intellchub.banke.ChatterApiInterface;
import com.intellchub.banke.models.AuthUserResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class LoginInteractorImpl implements LoginInteractor {


    @Override
    public void login(String email, String username, String password, final OnLoginFinishedListener onLoginFinishedListener, ChatterApiInterface chatterApiInterface) {

        boolean error = false;
        if (TextUtils.isEmpty(username)){
            onLoginFinishedListener.onUsernameError();
            error = true;
            return;
        }

        if (TextUtils.isEmpty(password)){
            onLoginFinishedListener.onPasswordError();
            error = true;
            return;
        }

        Call<AuthUserResponse> call = chatterApiInterface.login(email, password);

        call.enqueue(new Callback<AuthUserResponse>() {
            @Override
            public void onResponse(Response<AuthUserResponse> response, Retrofit retrofit) {
                AuthUserResponse authUserResponse = response.body();
                onLoginFinishedListener.onSuccess();
                if (response.isSuccess()) {
                    Log.i("DEBUG", response.body().toString());
                    /*Snackbar.make(view,"Data retrieved", Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();*/
                } else {
                    Log.i("ERROR", String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("LOGIN FAILURE", "Failure: "+t.getMessage());
                onLoginFinishedListener.onFailure();
            }
        });

    }
}
