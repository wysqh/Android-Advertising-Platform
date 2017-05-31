package com.example.gtk.platform.model;

/**
 * Created by gutia on 2017-05-30.
 */

public class UserInfoModel {
    private String userAcc;
    private String userPass;

    public UserInfoModel() {
    }

    public UserInfoModel(String userAcc, String userPass) {
        this.userAcc = userAcc;
        this.userPass = userPass;
    }

    public String getUserAcc() {
        return userAcc;
    }

    public void setUserAcc(String userAcc) {
        this.userAcc = userAcc;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
