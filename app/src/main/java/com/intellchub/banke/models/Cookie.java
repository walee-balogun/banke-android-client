package com.intellchub.banke.models;

/**
 * Created by Adewale_MAC on 25/03/2017.
 */

public class Cookie {
    private Object originalMaxAge;
    private Object expires;
    private boolean secure;
    private boolean httpOnly;
    private String path;

    public Object getOriginalMaxAge() {
        return originalMaxAge;
    }

    public void setOriginalMaxAge(Object originalMaxAge) {
        this.originalMaxAge = originalMaxAge;
    }

    public Object getExpires() {
        return expires;
    }

    public void setExpires(Object expires) {
        this.expires = expires;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
