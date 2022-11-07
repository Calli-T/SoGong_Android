package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("uid")
    private String UserId;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("auto_login")
    private boolean auto_login;

    public String getEmail() {
        return email;
    }

    public boolean isAuto_login() {
        return auto_login;
    }

    public void setAuto_login(boolean auto_login) {
        this.auto_login = auto_login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", UserId='" + UserId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", auto_login=" + auto_login +
                '}';
    }
}
