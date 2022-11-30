package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.LikeInfo;
import com.example.sogong.Model.Report;
import com.example.sogong.View.RecipeLookupActivity;
import com.example.sogong.View.ReportActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlReport_f {
    public void reportPost(Report reportInfo) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call;
        Log.d("reportPost", reportInfo.toString());
        if (reportInfo.getPost_type() == 1) call = sv.ReportRecipePost(reportInfo);
        else call = sv.ReportPhotoPost(reportInfo);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                ReportActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) { // 502
                ReportActivity.responseCode = 502;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void reportComment(Report reportInfo) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.ReportComment(reportInfo);
        Log.d("reportComment", reportInfo.toString());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                ReportActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else {
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) { // 500
                ReportActivity.responseCode = 502;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

}
