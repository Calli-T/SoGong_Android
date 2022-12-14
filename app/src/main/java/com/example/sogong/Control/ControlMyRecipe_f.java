package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.SearchInfo;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.View.MyPageBoardActivity;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RecipeSearchResultActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlMyRecipe_f {
    List<RecipePost_f> recipeList;

    public void lookupMyRecipeList(String nickname) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<RecipePost_f>> call = sv.LookupMyRecipeList(nickname);
        Log.d("lookup My RecipeList", "nickname = " + nickname);
        call.enqueue(new Callback<List<RecipePost_f>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePost_f>> call, @NonNull Response<List<RecipePost_f>> response) {
                MyPageBoardActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            MyPageBoardActivity.recipelist = response.body();
                            //Log.d("result", recipeList.toString());
                        }
                    }
                } else { // 400
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePost_f>> call, @NonNull Throwable t) { // 500
                MyPageBoardActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void searchMyRecipeList(SearchInfo searchInfo) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<RecipePost_f>> call = sv.SearchMyRecipeList(searchInfo);
        Log.d("search My RecipeList", searchInfo.toString());

        call.enqueue(new Callback<List<RecipePost_f>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePost_f>> call, @NonNull Response<List<RecipePost_f>> response) {
                RecipeSearchResultActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            recipeList = response.body();
                            Log.d("result", recipeList.toString());
                            RecipeSearchResultActivity.recipeList = response.body();
                        }
                    }
                } else { // 400
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePost_f>> call, @NonNull Throwable t) { // 500
                RecipeSearchResultActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void sortMyRecipeList(SortInfo sortInfo) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<RecipePost_f>> call = sv.SortMyRecipeList(sortInfo);
        Log.d("sort My RecipeList", sortInfo.toString());
        call.enqueue(new Callback<List<RecipePost_f>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePost_f>> call, @NonNull Response<List<RecipePost_f>> response) {
                MyPageBoardActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            recipeList = response.body();
                            Log.d("result", recipeList.toString());
                            MyPageBoardActivity.recipelist = response.body();
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePost_f>> call, @NonNull Throwable t) { // 500
                MyPageBoardActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void showRecipeList(List<RecipePost_f> recipeList) {
    }

    public void noResult(String message) {
    }

}
