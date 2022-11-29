package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.Refri_AddIngredientActivity;
import com.example.sogong.View.RefrigeratorActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        Log.d("lookup Refrigerator", "nickname = " + nickname);

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
        Log.d("add Refrigerator",ingredient.toString());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                Refri_AddIngredientActivity.responseCode = response.code();

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
        Log.d("delete Refrigerator","id = "+id);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                RefrigeratorActivity.responseCode = response.code();

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
        Log.d("edit Refrigerator",ingredient.toString());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                Refri_AddIngredientActivity.responseCode = response.code();

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

    public long expireDateWarning(String expireDate) throws ParseException {
        Date now = new Date();
        Date expire = new SimpleDateFormat("yyyy-MM-dd").parse(expireDate);
        long diffSec = (expire.getTime() - now.getTime()) / 1000; //초 차이
        long remainDay = diffSec / (24*60*60);
        Log.d("남은 일자", String.valueOf(remainDay));
        return remainDay;
    }

    public void showRefrigerator(List<Recipe_Ingredients> refrigerator) {
    }

    public void noResult(String message) {


    }//이거랑 control의 startToast랑 뭐가 다름?
    // -> 화면에 텍스트뷰에다가 메세지를 입력해서 보여주는거에여 쀼잉><
}
