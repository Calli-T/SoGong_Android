package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoList;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.View.PhotoFragment;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlPhotoList_f {

    PhotoList photoList = new PhotoList();

    public void lookupPhotoList(int page){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<PhotoList> call = sv.LookupPhotoList(page);

        call.enqueue(new Callback<PhotoList>() {
            @Override
            public void onResponse(@NonNull Call<PhotoList> call, @NonNull Response<PhotoList> response) {
                // getter를 각각 만들어 두면 서류는 늘어나지만 static 안써도됩니다.
                PhotoFragment.responseCode = response.code();

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            photoList = response.body();
                            Log.d("result", photoList.toString());

                            // getter를 각각 만들어 두면 서류는 늘어나지만 static 안써도됩니다.
                            PhotoFragment.photoList = photoList.getPhotoList();
                            PhotoFragment.totalpage = photoList.getTotal_page();
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<PhotoList> call, @NonNull Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void sortPhotoList(String sortBy, int page) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        SortInfo sortInfo = new SortInfo(page, sortBy);
        Call<List<PhotoPost>> call = sv.SortPhotoList(sortInfo);

        call.enqueue(new Callback<List<PhotoPost>>() {
            @Override
            public void onResponse(Call<List<PhotoPost>> call, Response<List<PhotoPost>> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            photoList.setPhotoList(response.body());
                            Log.d("result", photoList.getPhotoList().toString());

                            // getter를 각각 만들어 두면 서류는 늘어나지만 static 안써도됩니다.
                            PhotoFragment.photoList = photoList.getPhotoList();
                            PhotoFragment.responseCode = response.code();
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(Call<List<PhotoPost>> call, Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void showList(List<PhotoPost> photoList){}
}
