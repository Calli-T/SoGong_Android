package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("uid")
    private String uid;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("auto_login")
    private boolean auto_login;

    //@SerializedName("code")
    //private int code;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuto_login() {
        return auto_login;
    }

    public void setAuto_login(boolean auto_login) {
        this.auto_login = auto_login;
    }

    public User(String nickname, String uid, String password, String email, boolean auto_login) {
        this.nickname = nickname;
        this.uid = uid;
        this.password = password;
        this.email = email;
        this.auto_login = auto_login;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", uid='" + uid + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", auto_login=" + auto_login +
                '}';
    }
}
