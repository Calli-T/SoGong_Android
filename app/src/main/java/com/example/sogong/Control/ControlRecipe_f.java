package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePostLookUp;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.View.PhotoFragment;
import com.example.sogong.View.RecipeAddActivity;
import com.example.sogong.View.RecipeLookupActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlRecipe_f {

    RecipePost_f recipePostF;
    RecipePostLookUp recipePostLookUp;

    public void lookupRecipe(int postId, String nickname) {

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<RecipePostLookUp> call = sv.LookupRecipe(postId, nickname);
        Log.d("lookup Recipe","postId = "+postId+" nickname = "+nickname);

        call.enqueue(new Callback<RecipePostLookUp>() {
            @Override
            public void onResponse(@NonNull Call<RecipePostLookUp> call, @NonNull Response<RecipePostLookUp> response) {
                RecipeLookupActivity.responseCode.set(response.code());

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            RecipeLookupActivity.recipePostLookUp = response.body();
                            recipePostLookUp = response.body();
                            Log.d("result", recipePostLookUp.toString());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipePostLookUp> call, @NonNull Throwable t) { // 502
                RecipeLookupActivity.responseCode.set(502);
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void addRecipe(RecipePost_f newRecipe) {

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<RecipePost_f> call = sv.AddRecipe(newRecipe);
        Log.d("add Recipe",newRecipe.toString());

        call.enqueue(new Callback<RecipePost_f>() {
            @Override
            public void onResponse(@NonNull Call<RecipePost_f> call, @NonNull Response<RecipePost_f> response) {
                RecipeAddActivity.responseCode = response.code();
                Log.d("받은 레시피", newRecipe.toString());
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            recipePostF = response.body();
                            Log.d("result", recipePostF.toString());
//                            RecipeAddActivity.newRecipe = response.body();
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipePost_f> call, @NonNull Throwable t) { // 502
                RecipeAddActivity.responseCode = 502;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void editRecipe(RecipePost_f edittedRecipe) {

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<RecipePost_f> call = sv.EditRecipe(edittedRecipe);
        Log.d("edit Recipe",edittedRecipe.toString());

        call.enqueue(new Callback<RecipePost_f>() {
            @Override
            public void onResponse(@NonNull Call<RecipePost_f> call, @NonNull Response<RecipePost_f> response) {
                // 200
                RecipeAddActivity.responseCode = response.code();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            recipePostF = response.body();
                            Log.d("result", recipePostF.toString());
                            RecipeAddActivity.newRecipe = response.body();
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipePost_f> call, @NonNull Throwable t) { // 502
                RecipeAddActivity.responseCode = 502;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void deleteRecipe(String nickname, int postId) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        RecipePost_f targetPost = new RecipePost_f();
        targetPost.setNickname(nickname);
        targetPost.setPost_id(postId);
        Call<Integer> call = sv.DeleteRecipe(targetPost);
        Log.d("delete Recipe",targetPost.toString());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                RecipeLookupActivity.responseCode.set(response.code());

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", ""+response.body());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 502
                RecipeLookupActivity.responseCode.set(502);
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void showRecipe(RecipePost_f recipePostF) {
    }

    public Boolean isMine(String nickname) {
        Boolean result = true;
        return result;
    }
}
