package com.example.sogong.Model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {
    @SerializedName("email")
    private String email;

    @SerializedName("code")
    private int code;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = Integer.parseInt(code);
    }

    // 2차에만 code가 있는데 아무값이나 넣으면됨
    public AuthInfo(String email, String code) {
        this.email = email;
        this.code = Integer.parseInt(code);
    }
}
