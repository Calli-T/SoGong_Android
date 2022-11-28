package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.User;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.MainActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlLogout_f {
    public void logout(){

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.Logout(ControlLogin_f.userinfo);
        Log.d("logout", ControlLogin_f.userinfo.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                MainActivity.responseCode = response.code();
                // 200
                /*if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            //userinfo = new User(response.body().getNickname(), response.body().getUid(), response.body().getPassword(), response.body().getEmail(), response.body().isAuto_login());
                            //LoginActivity.responseCode = response.code();
                            autoLoginDisabled("muyaho"); // 자동로그인 해제 로직이 정해지면 만듭시다
                        }
                    } else // 404
                        Log.d("404 Not Found", "with logout");
                } else {
                    // 400 or 500

                    //LoginActivity.responseCode = response.code();
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void autoLoginDisabled(String nickname){
        ControlLogin_f.userinfo.setAuto_login(false);
    }
}
