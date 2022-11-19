package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.SearchInfo;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRecipeList_f {

    RecipeList recipeList = new RecipeList();

    public void lookupRecipeList(int page){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<RecipeList> call = sv.LookupRecipeList(page);

        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(@NonNull Call<RecipeList> call, @NonNull Response<RecipeList> response) {
                /*RecipeFragment.responseCode = response.code();
                RecipeFragment.recipelist = response.body().getRecipeList();
                RecipeFragment.totalpage = response.body().getTotal_page();*/

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            recipeList = response.body();
                            Log.d("result", ""+recipeList.getRecipeList().size());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipeList> call, @NonNull Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void searchRecipeList(String searchType, String categories, String keywordType, String keyword, int page){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        SearchInfo searchInfo = new SearchInfo(searchType, categories, keywordType, keyword, page, "");
        Call<RecipeList> call = sv.SearchRecipeList(searchInfo);

        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            recipeList = response.body();
                            Log.d("result", ""+recipeList.getTotal_page());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void sortRecipeList(String sortBy, int page){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        SortInfo sortInfo = new SortInfo(page, sortBy);
        Call<List<RecipePost_f>> call = sv.SortRecipeList(sortInfo);

        call.enqueue(new Callback<List<RecipePost_f>>() {
            @Override
            public void onResponse(Call<List<RecipePost_f>> call, Response<List<RecipePost_f>> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            recipeList.setRecipeList(response.body());
                            Log.d("result", recipeList.getRecipeList().toString());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(Call<List<RecipePost_f>> call, Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void showList(List<RecipePost_f> recipeList){}
    public void noResult(String message){}
}