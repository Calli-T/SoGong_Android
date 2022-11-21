package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.LikeInfo;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.Post;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.View.RecipeLookupActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlLike_f {
    public void isLiked(String nickname, Post post) {
    }

    public void likePost(String nickname, int postType, int postId) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        LikeInfo likeInfo = new LikeInfo(0, nickname, postType, postId, "등록");
        Call<Integer> call;
        if (likeInfo.getPostType() == 1) call = sv.LikeRecipePost(likeInfo);
        else call = sv.LikePhotoPost(likeInfo);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                RecipeLookupActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 501
                    Log.d("result", "등록 오류");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void unLikePost(String nickname, int postType, int postId) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        LikeInfo likeInfo = new LikeInfo(0, nickname, postType, postId, "취소");
        Call<Integer> call;
        if (likeInfo.getPostType() == 1) call = sv.UnLikeRecipePost(likeInfo);
        else call = sv.UnLikePhotoPost(likeInfo);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                RecipeLookupActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 500
                    Log.d("result", "취소 실패");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
}
