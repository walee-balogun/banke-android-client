package com.intellchub.banke.di.components;

import android.content.SharedPreferences;

import com.intellchub.banke.di.modules.AppModule;
import com.intellchub.banke.di.modules.NetModule;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.Retrofit;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */
@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    Retrofit retrofit();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}
