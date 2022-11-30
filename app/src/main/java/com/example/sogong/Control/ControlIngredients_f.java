package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.View.RecipeLookupActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlIngredients_f {

    List<Recipe_Ingredients> unExistIngredients;

    public void lookupUnExistIngredients(String nickname, int postId) {
        Log.d("받은 nickname과 postId","nickname = "+ nickname + " postId = "+postId);
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<Recipe_Ingredients>> call = sv.LookupUnExistIngredients(nickname, postId);

        call.enqueue(new Callback<List<Recipe_Ingredients>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe_Ingredients>> call, @NonNull Response<List<Recipe_Ingredients>> response) {
                RecipeLookupActivity.responseCode2.set(response.code());

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            unExistIngredients = response.body();
                            Log.d("result", unExistIngredients.toString());
                            RecipeLookupActivity.unExistIngredients = response.body();
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Recipe_Ingredients>> call, @NonNull Throwable t) { // 502
                RecipeLookupActivity.responseCode2.set(502);
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void remainAmmounts(String nickname, int postId){
        Log.d("받은 nickname과 postId","nickname = "+ nickname + " postId = "+postId);
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.RemainAmmounts(nickname, postId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                RecipeLookupActivity.responseCode3.set(response.code());

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            Log.d("result", ""+response.body());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 502
                RecipeLookupActivity.responseCode3.set(502);
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
}
