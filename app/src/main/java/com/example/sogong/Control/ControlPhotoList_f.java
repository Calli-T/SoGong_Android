package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoList;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlPhotoList_f {
    List<PhotoPost> photoList;
    public void lookupPhotoList(int page){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<PhotoList> call = sv.LookupPhotoList(page);

        call.enqueue(new Callback<PhotoList>() {
            @Override
            public void onResponse(@NonNull Call<PhotoList> call, @NonNull Response<PhotoList> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            Log.d("result", response.body().toString());
                        }
                    } else { // 500
                        Log.d("result", "디비 오류");
                    }
                } else { // 502
                    Log.d("result", "알 수 없는 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoList> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
                Log.d("result", t.getMessage());
            }
        });
    }
    public void sortPhotoList(String sortBy, int page){}
    public void showList(List<PhotoPost> photoList){}
}
