package com.intellchub.banke.di.components;

import com.intellchub.banke.chat.ChatActivity;
import com.intellchub.banke.di.modules.ChatterModule;
import com.intellchub.banke.di.scopes.UserScope;
import com.intellchub.banke.login.LoginActivity;

import dagger.Component;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */
@UserScope
@Component(dependencies = NetComponent.class, modules = ChatterModule.class)

public interface ChatterComponent {
    void inject(LoginActivity activity);
    void inject(ChatActivity activity);

}
