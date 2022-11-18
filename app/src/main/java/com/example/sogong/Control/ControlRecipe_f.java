package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.AuthInfo;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.Model.User;
import com.example.sogong.View.EmailVerificationActivity;
import com.example.sogong.View.HomeFragment;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.RecipeLookupActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRecipe_f {
    RecipePost recipePost;

    public void lookupRecipe(int postId) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<RecipePost> call = sv.GetRecipePost(postId);
        call.enqueue(new Callback<RecipePost>() {
            @Override
            public void onResponse(@NonNull Call<RecipePost> call, @NonNull Response<RecipePost> response) {
                //.responseCode = response.code();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            RecipeLookupActivity.responseCode = response.code();
                            RecipeLookupActivity.recipePost = response.body();
                        }
                    } else // 404
                        Log.d("404 Not Found", "with login");
                } else {
                    // 400 or anything
                    RecipeLookupActivity.responseCode = response.code();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipePost> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void addRecipe(RecipePost newRecipe) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.AddRecipe(newRecipe);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                //.responseCode = response.code();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void editRecipe(RecipePost edittedRecipe) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.EditRecipe(edittedRecipe);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                //.responseCode = response.code();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void deleteRecipe(String nickname, int postId) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.DeleteRecipe(nickname, postId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                //.responseCode = response.code();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void showRecipe(RecipePost recipePost) {
    }

    public Boolean isMine(String nickname) {
        Boolean result = true;
        return result;
    }

    /*
    public void testLookup(int postId) {
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<RecipePost> call = sv.test(postId);
        call.enqueue(new Callback<RecipePost>() {
            @Override
            public void onResponse(@NonNull Call<RecipePost> call, @NonNull Response<RecipePost> response) {
                HomeFragment.str = response.code() +"";//response.body().toString();
            }

            @Override
            public void onFailure(@NonNull Call<RecipePost> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
    */

    public void testStringLookup(int postId){
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.testString(postId);

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
}
