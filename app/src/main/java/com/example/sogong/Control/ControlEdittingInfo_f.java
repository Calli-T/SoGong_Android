package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.AuthInfo;
import com.example.sogong.Model.User;
import com.example.sogong.View.ChangeNicknameActivity;
import com.example.sogong.View.ChangePasswordActivity;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlEdittingInfo_f {
    public void editPassword(String pw){
        User old = ControlLogin_f.userinfo;
        User newUser = new User(old.getNickname(), old.getUid(), pw, old.getEmail(), old.isAuto_login());

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.EditPassword(newUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // 200
                ChangePasswordActivity.responseCode = response.code();
                if (response.isSuccessful()) {
                    /*if (response.body() != null) {
                        if (response.code() == 200) {
                            ControlLogin_f.userinfo.setPassword(pw);
                            ChangePasswordActivity.responseCode = response.code();
                        }
                    } else // 404
                        Log.d("404 Not Found", "with editPassword");
                    */
                } else {
                    // 500 or 502

                    //LoginActivity.responseCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void editNickname(String new_nickname){

        User old = ControlLogin_f.userinfo;
        User newUser = new User(new_nickname, old.getUid(), old.getPassword(), old.getEmail(), old.isAuto_login());

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.EditNickname(newUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ChangeNicknameActivity.responseCode = response.code();
                // 200
                /*if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            //ControlLogin_f.userinfo = new User(response.body().getNickname(), response.body().getUid(), response.body().getPassword(), response.body().getEmail(), response.body().isAuto_login());
                            ControlLogin_f.userinfo.setNickname(new_nickname);
                            //LoginActivity.responseCode = response.code();
                        }
                    } else // 404
                        Log.d("404 Not Found", "with editNickname");
                } else {
                    // 400 or more

                    //LoginActivity.responseCode = response.code();
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
