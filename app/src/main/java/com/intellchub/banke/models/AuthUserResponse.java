package com.intellchub.banke.models;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class AuthUserResponse {
    private boolean success;
    private Session session;
    private String idToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
