package com.intellchub.banke.models;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class Session {
    private Cookie cookie;
    private Passport passport;
    private User user;

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
