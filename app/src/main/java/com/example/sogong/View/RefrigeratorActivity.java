package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.Model.Report;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class RefrigeratorActivity extends AppCompatActivity {
    public Refri_Ingre_Adapter refri_ingre_Adapter;
    public RecyclerView refrigeratorRecyclerview;
    public static List<Refrigerator> ingreList;
    public static int responseCode;
    private boolean threadFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        refrigeratorRecyclerview = (RecyclerView) findViewById(R.id.ingredients_recyclerview);

        refri_ingre_Adapter = new Refri_Ingre_Adapter();

        refrigeratorRecyclerview.setAdapter(refri_ingre_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RefrigeratorActivity.this);
        refrigeratorRecyclerview.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.ingre_add_button);
        fab.setOnClickListener(new FABClickListener());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    refri_ingre_Adapter.setRefriIngreList(ingreList);

                    //레시피 리사이클러뷰 클릭 이벤트
                    refri_ingre_Adapter.setOnItemClickListener(new Refri_Ingre_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
//                            Intent intent = new Intent(RefrigeratorActivity.this, IngredientLookupActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            intent.putExtra("recipe_post", ingreList.get(position));
//                            startActivity(intent);
                        }
                    });
                    //재료리스트에 있는 버튼들의 클릭 이벤트 처리
                    //재료 수정
                    refri_ingre_Adapter.setOnItemLeftButtonClickListener(new Refri_Ingre_Adapter.OnItemLeftButtonClickListener() {
                        @Override
                        public void onItemLeftButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "left button click");

                        }
                    });
                    //재료 삭제
                    refri_ingre_Adapter.setOnItemRightButtonClickListener(new Refri_Ingre_Adapter.OnItemRightButtonClickListener() {
                        @Override
                        public void onItemRightButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "right button click");

                        }
                    });


                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

                    if (threadFlag)
                        runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intent = new Intent(RefrigeratorActivity.this, RecipeAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
}
