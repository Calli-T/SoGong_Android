package com.example.sogong.Control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sogong.Model.User;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.MainActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlLogin_f {
    public static User userinfo;

    public String hashCode(String pw) {
        int[] temp = new int[pw.length()];
        for (int i = 0; i < pw.length(); i++) {
            temp[i] = (int) pw.toCharArray()[i] + 1;
        }
        char[] temp_str = new char[pw.length()];
        for (int i = 0; i < pw.length(); i++) {
            temp_str[i] = (char)temp[i];
        }
        String pwHash = String.valueOf(temp_str);
        Log.d("해쉬", pwHash);
        return pwHash;
    }

    public void login(String id, String pw, boolean isAutoLogin) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<User> call = sv.Login(new User(null, id, pw, null, isAutoLogin));
        Log.d("받은 userInfo", "id = " + id + " pw = " + pw + " isAutoLogin = " + isAutoLogin);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                LoginActivity.responseCode = response.code();


                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            userinfo = new User(response.body().getNickname(), response.body().getUid(), response.body().getPassword(), response.body().getEmail(), response.body().isAuto_login());
                        }
                    } else // 404
                        Log.d("404 Not Found", "with login");
                } else { // 400 or
                    LoginActivity.responseCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LoginActivity.responseCode = 500;
            }
        });

    }
}
