package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMyPhoto_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPost_f;
import com.example.sogong.Control.ControlRefrigerator_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.Model.Report;
import com.example.sogong.Model.SearchInfo;
import com.example.sogong.Model.SortInfo;
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
    public static int responseResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        responseCode = 0;

        threadFlag = true;

        // UI controller
        Refrigerator_UI rfu = new Refrigerator_UI();


        refrigeratorRecyclerview = (RecyclerView) findViewById(R.id.ingredients_recyclerview);

        refri_ingre_Adapter = new Refri_Ingre_Adapter();

        refrigeratorRecyclerview.setAdapter(refri_ingre_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RefrigeratorActivity.this);
        refrigeratorRecyclerview.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.ingre_add_button);
        fab.setOnClickListener(new FABClickListener());

        // 원래 있던 요리 재료 가져오는 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rfu.startToast("muyaho");
                
                if (responseCode == 200) {
                    responseCode = -1;
                    refri_ingre_Adapter.setRefriIngreList(ingreList);
                    rfu.startToast(ingreList.get(1).toString());

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

        ControlRefrigerator_f crff = new ControlRefrigerator_f();
        crff.lookupRefrigerator("test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlPost_f cpf = new ControlPost_f();
        cpf.lookupMyCommentList("test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag = false;
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

    class Refrigerator_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(RefrigeratorActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RefrigeratorActivity.this, RefrigeratorActivity.class);
                startActivity(intent);
            }
        }
    }
}

// #7
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlRefrigerator_f crff = new ControlRefrigerator_f();
        Refrigerator refri = new Refrigerator(0, "yangpa", "jin92", "g", 200.0F, "2023-12-31");
        crff.addRefrigerator(refri);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

 */

// #8
// 작동 안됨, 500

// #9
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlRefrigerator_f crff = new ControlRefrigerator_f();
        Refrigerator refri = new Refrigerator(20, "asparagus", "test", "g", 200.0F, "2023-12-31");
        crff.editRefrigerator(refri);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #11
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlMyPhoto_f cmpf = new ControlMyPhoto_f();
        cmpf.lookupMyPhotoList("test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #12
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
        cmrf.lookupMyRecipeList("test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #13
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
        cmrf.searchMyRecipeList(new SearchInfo("", "", "", "test0", 1, "test")); // 공백x3, 키워드, 페이지, 닉네임 순서

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #14
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
        cmrf.sortMyRecipeList(new SortInfo("test", "좋아요 순")); // 닉네임, 정렬 기준의 순서/ 기준은  "조회수 순" | "최신글 순" | "좋아요 순"

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #15
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlPost_f cpf = new ControlPost_f();
        cpf.lookupMyLikeList("test", 1); // 레시피의 경우
        //cpf.lookupMyLikeList("test", -1); // 사진의 경우, 아직 버그있음

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// #16
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if (responseCode == 500) {

                } else if (responseCode == 502) {

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

        ControlPost_f cpf = new ControlPost_f();
        cpf.lookupMyCommentList("test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// 0단계: 주석 보고 주석 써놓기

// 1단계
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    rlu.startToast();
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
                        getActivity().runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(2);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// 2단계: 컨트롤에서 static 건드리기

// 3단계: 테스트

// 4단계: 원래 있어야할 장소에 코드 냅두기