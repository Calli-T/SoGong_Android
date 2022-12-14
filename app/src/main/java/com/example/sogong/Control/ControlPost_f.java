package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.Post;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.View.MyPageBoardActivity;
import com.example.sogong.View.PhotoFragment;
import com.example.sogong.View.RecipeAddActivity;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlPost_f {
    List<Post> postList;
    List<RecipePost_f> recipeList;
    List<PhotoPost> photoList;

    public void lookupMyLikeList(String nickname, int postType) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<RecipePost_f>> callr;
        Call<List<PhotoPost>> callp;
        Log.d("lookup My LikeList", "nickname = " + nickname + " postType = " + postType);
        if (postType == 1) {
            callr = sv.LookupMyRecipeLikeList(nickname, postType);
            callr.enqueue(new Callback<List<RecipePost_f>>() {
                @Override
                public void onResponse(@NonNull Call<List<RecipePost_f>> call, @NonNull Response<List<RecipePost_f>> response) {
                    MyPageBoardActivity.responseCode = response.code();

                    // 200
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.code() == 200) {
                                MyPageBoardActivity.recipelist = response.body();
                            }
                        }
                    } else { // 405
                        Log.d("result", "디비 오류");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RecipePost_f>> call, @NonNull Throwable t) { // 500
                    MyPageBoardActivity.responseCode = 500;
                    Log.d("result", "알 수 없는 오류");
                }
            });
        } else {
            callp = sv.LookupMyPhotoLikeList(nickname, postType);
            callp.enqueue(new Callback<List<PhotoPost>>() {
                @Override
                public void onResponse(@NonNull Call<List<PhotoPost>> call, @NonNull Response<List<PhotoPost>> response) {
                    MyPageBoardActivity.responseCode = response.code();

                    // 200
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.code() == 200) {
                                MyPageBoardActivity.photolist = response.body();

                                photoList = response.body();
                                Log.d("result", photoList.toString());
                            }
                        }
                    } else { // 405
                        Log.d("result", "디비 오류");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<PhotoPost>> call, @NonNull Throwable t) { // 500
                    MyPageBoardActivity.responseCode = 500;
                    Log.d("result", "알 수 없는 오류");
                }
            });
        }
    }

    public void lookupMyCommentList(String nickname) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<RecipePost_f>> call = sv.LookupMyCommentList(nickname);
        Log.d("lookup My CommentList","nickname = "+nickname);

        call.enqueue(new Callback<List<RecipePost_f>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePost_f>> call, @NonNull Response<List<RecipePost_f>> response) {
                MyPageBoardActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            recipeList = response.body();
                            MyPageBoardActivity.recipelist = response.body();
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

    public void showList(List<Post> postList) {
    }

    public void noResult(String message) {
    }
}
