package com.example.sogong.Control;

import com.example.sogong.Model.Ingredients;
import com.example.sogong.Model.User;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRefrigerator_f {
    List<Ingredients> refrigerator;
    public void lookupRefrigerator(String nickname){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Ingredients> call = sv.LookupIngredientsList(nickname);
        call.enqueue(new Callback<Ingredients>() {
            @Override
            public void onResponse(Call<Ingredients> call, Response<Ingredients> response) {
                //200
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<Ingredients> call, Throwable t) {

            }
        });

    }
    public void addRefrigerator(String nickname, Ingredients ingredient){}
    public void deleteRefrigerator(String nickname, String name){}
    public void editRefrigerator(String nickname, Ingredients ingredient){}
    public void expireDateWarning(){}
    public void showRefrigerator(List<Ingredients> refrigerator){}
    public void noResult(String message){}//이거랑 control의 startToast랑 뭐가 다름?
}