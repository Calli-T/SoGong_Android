package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.User;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;
import com.example.sogong.View.SignupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlSignup_f {
    public void signUp(User userinfo){

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.SignUp(userinfo);
        Log.d("signUp",userinfo.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                SignupActivity.responseCode = response.code();
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
/*
        retrofitService = com.example.sogong.View.Legacy.RetrofitClient.getClient().create(RetrofitService.class);
        Call<User> call = retrofitService.setSignUp(userinfo);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Control control = new Control();
//                    control.changePage();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        */