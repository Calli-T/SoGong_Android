package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMailList_f;
import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MailBoxActivity extends AppCompatActivity {

    String[] pagenum;
    public static int totalpage;
    public static List<Mail> maillist;
    public static int responseCode = 0;
    public MailAdapter mailAdapter;
    public RecyclerView mailRecyclerView;
    Custon_ProgressDialog custon_progressDialog;
    private MailBoxActivity_UI mui = new MailBoxActivity_UI();
    ImageButton back_button;

    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean();
    boolean firstpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        FloatingActionButton fab = findViewById(R.id.mail_add_button);
        fab.setOnClickListener(new MailBoxActivity.FABClickListener());

        TextView noResult = findViewById(R.id.noResult);
        noResult.setVisibility(View.INVISIBLE);
        mailRecyclerView = (RecyclerView) findViewById(R.id.mail_recyclerview);

        mailAdapter = new MailAdapter();

        mailRecyclerView.setAdapter(mailAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MailBoxActivity.this);
        mailRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(MailBoxActivity.this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();

        threadFlag.set(true);
        firstpage = true;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    mailAdapter.setRecipeList(maillist);
                    threadFlag.set(false);
                    pagenum = new String[totalpage];

                    if (maillist.size() == 0) {//쪽지가 없는 경우
                        TextView noResult = findViewById(R.id.noResult);
                        noResult.setVisibility(View.VISIBLE);
                    } else {
                        TextView noResult = findViewById(R.id.noResult);
                        noResult.setVisibility(View.INVISIBLE);
                    }

                    for (int i = 1; i <= totalpage; i++) {
                        pagenum[i - 1] = String.valueOf(i);
                    }//전체 페이지 수 만큼 String 배열 생성

                    //페이지 수 스피너 설정
                    Spinner pagespinner = findViewById(R.id.mailbox_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MailBoxActivity.this, android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pagespinner.setAdapter(adapter1);
                    pagespinner.setPrompt("이동할 페이지");

                    pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                ControlMailList_f cmlf = new ControlMailList_f();
                                cmlf.lookupMailList(position + 1, ControlLogin_f.userinfo.getNickname());
                                custon_progressDialog.show();

                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseCode == 200) {
                                            responseCode = -1;
                                            pagethreadFlag.set(false);
                                            //업데이트된 레시피 리스트로 전환
                                            mailAdapter.setRecipeList(maillist);
                                            //레시피 리사이클러뷰 클릭 이벤트
                                            mailAdapter.setOnItemClickListener(new MailAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(int position, String data) {
                                                    Intent intent = new Intent(MailBoxActivity.this, MailLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("mail", maillist.get(position));
                                                    startActivity(intent);
                                                    //+조회수 관련 로직 추가할 것
                                                }
                                            });
                                            custon_progressDialog.dismiss();
                                        }

                                    }
                                };
                                class NewRunnable implements Runnable {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 30; i++) {
                                            try {
                                                sleep(1000);
                                                Log.d("thread2", "thread2");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (pagethreadFlag.get()) {
                                                runOnUiThread(runnable);
                                                Log.d("thread2", "thread3");
                                            } else {
                                                i = 30;
                                                Log.d("thread2", "thread4");
                                            }
                                        }
                                        Log.d("thread2", "thread1");
                                    }
                                }
                                NewRunnable nr = new NewRunnable();
                                Thread t = new Thread(nr);
                                pagethreadFlag.set(true);
                                threadFlag.set(true);
                                //페이지 이동을 확인하는 쓰레드 실행
                                t.start();
                                //업데이트된 레시피 리스트로 전환
                                mailAdapter.setRecipeList(maillist);
                                //레시피 리사이클러뷰 클릭 이벤트
                                mailAdapter.setOnItemClickListener(new MailAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(MailBoxActivity.this, MailLookupActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("mail", maillist.get(position));
                                        startActivity(intent);
                                    }
                                });
                            }
                            firstpage = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //메일 리사이클러뷰 클릭 이벤트
                    mailAdapter.setOnItemClickListener(new MailAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(MailBoxActivity.this, MailLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("mail", maillist.get(position));
                            startActivity(intent);
                        }
                    });
                    custon_progressDialog.dismiss();

                } else if (responseCode == 404 || responseCode == 500) {
                    if (maillist.size() == 0) {//쪽지가 없는 경우
                        TextView noResult = findViewById(R.id.noResult);
                        noResult.setVisibility(View.VISIBLE);
                    } else {
                        TextView noResult = findViewById(R.id.noResult);
                        noResult.setVisibility(View.INVISIBLE);
                    }

                    List<String> temp = new ArrayList<>();
                    temp.add("확인");

                    mui.startDialog(0, "쪽지 조회 실패", "쪽지를 가져오는데 실패했습니다.", temp);
                }
            }
        };

        // 추가) 쪽지함 조회 호출 코드
        class NewRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (threadFlag.get())
                        runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }

        ControlMailList_f cmlf = new ControlMailList_f();
        cmlf.lookupMailList(1, ControlLogin_f.userinfo.getNickname());
        threadFlag.set(true);
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        //1페이지 쪽지 보는 쓰레드
        t.start();
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intent = new Intent(MailBoxActivity.this, MailSendActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    class MailBoxActivity_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(MailBoxActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
        }
    }
}
