package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RefrigeratorActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRefrigerator_f {
    List<Refrigerator> refrigerator;

    public void lookupRefrigerator(String nickname) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<Refrigerator>> call = sv.LookupRefrigerator(nickname);

        call.enqueue(new Callback<List<Refrigerator>>() {
            @Override
            public void onResponse(@NonNull Call<List<Refrigerator>> call, @NonNull Response<List<Refrigerator>> response) {
                RefrigeratorActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            refrigerator = response.body();
                            RefrigeratorActivity.ingreList = response.body();

                            Log.d("result", refrigerator.toString());
                        }
                    } else { // 404
                        Log.d("result", "사용자 냉장고 재료 없음");
                    }
                } else { // 401
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Refrigerator>> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void addRefrigerator(Refrigerator ingredient) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.AddRefrigerator(ingredient);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 400
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void deleteRefrigerator(int id) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Map<String, Integer> deleteInfo = new HashMap<>();
        deleteInfo.put("id", id);
        Call<Integer> call = sv.DeleteRefrigerator(deleteInfo);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 406
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void editRefrigerator(Refrigerator ingredient) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.EditRefrigerator(ingredient);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 406
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void expireDateWarning() {
    }

    public void showRefrigerator(List<Recipe_Ingredients> refrigerator) {
    }

    public void noResult(String message) {
    }//이거랑 control의 startToast랑 뭐가 다름?
    // -> 화면에 텍스트뷰에다가 메세지를 입력해서 보여주는거에여 쀼잉><
}
