package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.Mail;
import com.example.sogong.Model.MailList;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.View.MailBoxActivity;
import com.example.sogong.View.RecipeFragment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlMailList_f {

    MailList mailList;

    public void lookupMailList(int page, String nickname) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<MailList> call = sv.LookupMailList(nickname, page);

        call.enqueue(new Callback<MailList>() {
            @Override
            public void onResponse(@NonNull Call<MailList> call, @NonNull Response<MailList> response) {
                MailBoxActivity.responseCode = response.code();

                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            mailList = response.body();
                            Log.d("result", mailList.getMailList().toString());
                            MailBoxActivity.list = mailList.getMailList();
                            MailBoxActivity.totalpage = mailList.getTotal_page();
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<MailList> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void showList(List<Mail> mailList){}
}
