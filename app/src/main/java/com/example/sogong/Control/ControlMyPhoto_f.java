package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.View.PhotoFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlMyPhoto_f {
    List<PhotoPost> photoList;
    public void lookupMyPhotoList(String nickname){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<PhotoPost>> call = sv.LookupMyPhotoList(nickname);

        call.enqueue(new Callback<List<PhotoPost>>() {
            @Override
            public void onResponse(@NonNull Call<List<PhotoPost>> call, @NonNull Response<List<PhotoPost>> response) {
                PhotoFragment.responseCode = response.code();

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            photoList = response.body();
                            Log.d("result", photoList.toString());

                            PhotoFragment.photoList = response.body();
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<PhotoPost>> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void showMyPhotoList(List<PhotoPost> photoList){}
    public void noResult(String message){}
}
