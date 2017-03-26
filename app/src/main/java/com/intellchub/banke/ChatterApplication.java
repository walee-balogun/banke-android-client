package com.intellchub.banke;

import android.app.Application;

import com.intellchub.banke.di.components.ChatterComponent;
import com.intellchub.banke.di.components.DaggerChatterComponent;
import com.intellchub.banke.di.components.DaggerNetComponent;
import com.intellchub.banke.di.components.NetComponent;
import com.intellchub.banke.di.modules.AppModule;
import com.intellchub.banke.di.modules.ChatterModule;
import com.intellchub.banke.di.modules.NetModule;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class ChatterApplication extends Application {

    private NetComponent mNetComponent;
    private ChatterComponent mChatterComponent;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.BASE_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(Constants.BASE_URL))
                .build();

        mChatterComponent = DaggerChatterComponent.builder()
                .netComponent(mNetComponent)
                .chatterModule(new ChatterModule())
                .build();

    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }


    public ChatterComponent getmChatterComponent() {
        return mChatterComponent;
    }
}
