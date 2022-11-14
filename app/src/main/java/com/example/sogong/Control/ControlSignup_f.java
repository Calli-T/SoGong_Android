package com.example.sogong.Control;

import com.example.sogong.Model.User;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlSignup_f {
    private  RetrofitService retrofitService;
    public void signUp(User userinfo){
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
    }
}
