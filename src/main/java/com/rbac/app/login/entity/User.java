package com.rbac.app.login.entity;


/**
 * 用户javabean
 *
 */
public class User {
    private String userId;
    private String password;
    private String userName;
    private String userIphone;
    private String iconFileId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIphone() {
        return userIphone;
    }

    public void setUserIphone(String userIphone) {
        this.userIphone = userIphone;
    }

    public String getIconFileId() {
        return iconFileId;
    }

    public void setIconFileId(String iconFileId) {
        this.iconFileId = iconFileId;
    }
}