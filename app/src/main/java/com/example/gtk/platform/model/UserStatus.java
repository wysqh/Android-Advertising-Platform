package com.example.gtk.platform.model;

/**
 * Created by gutia on 2017-06-16.
 */

public class UserStatus {
    private String name;
    private boolean bLogin;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isbLogin() {
        return bLogin;
    }

    public void setbLogin(boolean bLogin) {
        this.bLogin = bLogin;
    }
}
