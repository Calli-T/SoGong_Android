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
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlMyPhoto_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPhoto_f;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RefrigeratorActivity extends AppCompatActivity {
    public Refri_Ingre_Adapter refri_ingre_Adapter;
    public RecyclerView refrigeratorRecyclerview;
    public static List<Refrigerator> ingreList; // 호출 응답 반환값 저장
    public static int responseCode; // 응답 코드
    private boolean threadFlag; // 스레드 제어용 플래그
    private boolean deletethreadFlag; // 스레드 제어용 플래그
    public static int responseResult;
    Custon_ProgressDialog custon_progressDialog;

    Boolean isProgress;

    // UI controller
    Refrigerator_UI rfu = new Refrigerator_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        responseCode = 0;

        threadFlag = true;

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
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
                    threadFlag = false;

                    //재료리스트에 있는 버튼들의 클릭 이벤트 처리
                    //재료 수정
                    refri_ingre_Adapter.setOnItemLeftButtonClickListener(new Refri_Ingre_Adapter.OnItemLeftButtonClickListener() {
                        @Override
                        public void onItemLeftButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "left button click");
                            Intent intent = new Intent(RefrigeratorActivity.this, Refri_AddIngredientActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("ingredient", ingreList.get(position));
                            intent.putExtra("edit", 1);
                            startActivity(intent);
                        }
                    });
                    //재료 삭제
                    refri_ingre_Adapter.setOnItemRightButtonClickListener(new Refri_Ingre_Adapter.OnItemRightButtonClickListener() {
                        @Override
                        public void onItemRightButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "right button click");
                            /* #8 사용자 보유 재료 삭제 */
                            rfu.startDialog(1, "재료 삭제", "해당 식재료를 삭제하시겠습니까?", new ArrayList<>(Arrays.asList("삭제", "취소")));

                            class NewRunnable implements Runnable {
                                NewRunnable() {
                                }

                                @Override
                                public void run() {
                                    while (true) {
                                        try {
                                            Thread.sleep(100);
                                            if (Custom_Dialog.state == 0) {
                                                Custom_Dialog.state = -1;
                                                isProgress = true;
                                                final Runnable progress = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (isProgress) {
                                                            custon_progressDialog.show();
                                                        } else custon_progressDialog.dismiss();
                                                        ;
                                                    }
                                                };
                                                runOnUiThread(progress);
                                                final Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (responseCode == 200) {
                                                            responseCode = -1;
                                                            deletethreadFlag = false;
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            Intent intent = getIntent();
                                                            finish(); //현재 액티비티 종료 실시
                                                            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                                                            startActivity(intent); //현재 액티비티 재실행 실시
                                                            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기

                                                        } else if (responseCode == 406) {
                                                            responseCode = -1;
                                                            deletethreadFlag = false;
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            rfu.startDialog(0, "삭제 실패", "삭제 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));

                                                        } else if (responseCode == 500) {
                                                            responseCode = -1;
                                                            deletethreadFlag = false;
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            rfu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                                        }
                                                    }
                                                };

                                                class NewRunnable1 implements Runnable {
                                                    @Override
                                                    public void run() {
                                                        for (int i = 0; i < 30; i++) {
                                                            try {
                                                                Thread.sleep(1000);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            if (deletethreadFlag)
                                                                runOnUiThread(runnable);
                                                            else {
                                                                i = 30;
                                                            }
                                                        }
                                                    }
                                                }
                                                deletethreadFlag = true;
                                                ControlRefrigerator_f crff = new ControlRefrigerator_f();
                                                crff.deleteRefrigerator(ingreList.get(position).getRefrigerator_id());
                                                NewRunnable1 nr1 = new NewRunnable1();
                                                Thread t = new Thread(nr1);
                                                t.start();

                                                break;
                                            } else if (Custom_Dialog.state == 1) {
                                                break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            deletethreadFlag = true;
                            NewRunnable nr = new NewRunnable();
                            Thread t = new Thread(nr);
                            t.start();

                        }
                    });
                    custon_progressDialog.dismiss();

                } else if (responseCode == 401) {
                    responseCode = -1;
                    threadFlag = false;
                    custon_progressDialog.dismiss();
                    rfu.startDialog(0, "서버 오류", "보유 재료 조회에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 404) {
                    responseCode = -1;
                    threadFlag = false;
                    custon_progressDialog.dismiss();
                    rfu.startToast("보유한 재료가 없습니다.");
                } else if (responseCode == 500) {
                    responseCode = -1;
                    threadFlag = false;
                    custon_progressDialog.dismiss();
                    rfu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
        crff.lookupRefrigerator(ControlLogin_f.userinfo.getNickname());
        threadFlag = true;
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
            Intent intent = new Intent(RefrigeratorActivity.this, Refri_AddIngredientActivity.class);
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
        
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RefrigeratorActivity.this, RefrigeratorActivity.class);
                startActivity(intent);
            }
        }
    }
}