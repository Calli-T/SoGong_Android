package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeLookupActivity extends AppCompatActivity {
    TextView recipetitle;
    TextView recipecategory;
    TextView recipespicy;
    TextView recipedescription;
    TextView recipeingre;
    TextView recipecomment;

    public static int responseCode;
    public static RecipePost recipePost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int post_id = getIntent().getIntExtra("recipe_post_id", 0);
        setContentView(R.layout.activity_lookuprecipe);
        recipetitle = findViewById(R.id.recipetitle_text1);
        recipecategory = findViewById(R.id.recipecate_text1);
        recipespicy = findViewById(R.id.recipespicy_text1);
        recipedescription = findViewById(R.id.recipedescription_text);

        responseCode = 0;

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    recipetitle.setText(recipePost.getTitle());
                    recipecategory.setText(recipePost.getCategory());
                    recipespicy.setText("X"+String.valueOf(recipePost.getDegree_of_spicy()));
                    recipedescription.setText(recipePost.getDescription());

                } else if (responseCode == 500) {
//                    rlu.startDialog(0,"서버 오류","서버 연결에 실패하였습니다.",new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 502) {
//                    rlu.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
                }
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(runnable);
                }
            }
        }
        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(post_id);
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    class RecipeLookup_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO 메시지가 표시되는 위치지정 (가운데 표시)
            //toast.setGravity(Gravity.TOP, 0, 0); //TODO 메시지가 표시되는 위치지정 (상단 표시)
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO 메시지가 표시되는 위치지정 (하단 표시)
            toast.setDuration(Toast.LENGTH_SHORT); //메시지 표시 시간
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(RecipeLookupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RecipeLookupActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                // 회원가입에서 요청한 이메일 인증
                EmailVerificationActivity.destination = 0;
                Intent intent = new Intent(RecipeLookupActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }
    }
}
