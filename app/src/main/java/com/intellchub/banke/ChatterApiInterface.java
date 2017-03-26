package com.intellchub.banke;

import com.intellchub.banke.models.AuthUserResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public interface ChatterApiInterface {

    @FormUrlEncoded
    @POST("users/api/login")
    Call<AuthUserResponse> login(@Field("username") String username, @Field("password") String password);
}
