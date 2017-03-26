package com.intellchub.banke.chat;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public interface ChatView {

    void showProgress();

    void showTyping();

    void showOffline();

    void showError();

    void showEmpty();

}
