package com.intellchub.banke.di.modules;

import com.intellchub.banke.ChatterApiInterface;
import com.intellchub.banke.di.scopes.UserScope;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */
@Module
public class ChatterModule {
    @Provides
    @UserScope
    public ChatterApiInterface providesCliqMiInterface(Retrofit retrofit) {
        return retrofit.create(ChatterApiInterface.class);
    }
}
