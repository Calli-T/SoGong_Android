package com.example.sogong.Control;

import androidx.annotation.NonNull;

import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.View.HomeFragment;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRecipeList_f {
    List<RecipeList> recipeList;
    public void lookupRecipeList(int page){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<RecipeList> call = sv.LookupRecipeList(page);

        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(@NonNull Call<RecipeList> call, @NonNull Response<RecipeList> response) {
                RecipeFragment.responseCode = response.code();
                RecipeFragment.list = response.body().getRecipeList();
                RecipeFragment.totalpage = response.body().getTotal_page();
            }

            @Override
            public void onFailure(@NonNull Call<RecipeList> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
    public void searchRecipeList(String searchType, String categories, String keywordType, String keyword, int page){}
    public void sortRecipeList(String sortBy, int page){}
    public void showList(List<RecipePost> recipeList){}
    public void noResult(String message){}

    /*
    public void testStringList(int page){
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.listString(page);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                HomeFragment.str = response.body().toString();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
    */
}
