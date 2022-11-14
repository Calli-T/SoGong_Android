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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlLogin_f {
    static User userinfo;

    public int hashCode(String pw) {
        return pw.hashCode();
    }

    public void login(String id, String pw, boolean isAutoLogin) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<User> call = sv.Login(new User(null, id, pw, null, isAutoLogin));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            userinfo = new User(response.body().getNickname(), response.body().getUid(), response.body().getPassword(), response.body().getEmail(), response.body().isAuto_login());
                            LoginActivity.responseCode = response.code();
                        }
                    } else // 404
                        Log.d("404 Not Found", "with login");
                } else {
                    // 400?
                    //String result = response.toString();
                    //String[] results = result.split(",");
                    //results[1].to;
                    LoginActivity.responseCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
