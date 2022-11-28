package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMail_f;
import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MailLookupActivity extends AppCompatActivity {

    public static int responseCode = 0;
    public static Mail mail;

    Boolean inProgress;

    TextView mailTitle;
    TextView mailAuthor;
    TextView mailDate;
    TextView mailDescription;
    Button deleteMail_Button;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    Custon_ProgressDialog custon_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookupmail);
        Mail mail = getIntent().getParcelableExtra("mail");
        mailTitle = findViewById(R.id.mailtitle_text1);
        mailAuthor = findViewById(R.id.mailauthor_text1);
        mailDate = findViewById(R.id.maildate_text1);
        mailDescription = findViewById(R.id.maildescription_text);
        deleteMail_Button = findViewById(R.id.delete_mail_button);
        mailTitle.setText(mail.getTitle());
        mailAuthor.setText(mail.getNickname());
        String date = mail.getSend_time().split("T")[0];
        mailDate.setText(date);
        mailDescription.setText(mail.getContents());
        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(MailLookupActivity.this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        MailLookupActivity_UI mlau = new MailLookupActivity_UI();
        deleteMail_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlau.startDialog(1, "쪽지 삭제", "쪽지를 삭제하시겠습니까?", new ArrayList<>(Arrays.asList("확인", "취소")));

                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        while(true){
                            try {
                                Thread.sleep(100);
                                if(Custom_Dialog.state== 0){
                                    Custom_Dialog.state = -1;
                                    inProgress = true;

                                    final Runnable progress = new Runnable() {
                                        @Override
                                        public void run() {
                                            if(inProgress){
                                                custon_progressDialog.show();
                                            } else custon_progressDialog.dismiss();
                                        }
                                    };
                                    runOnUiThread(progress);
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if(MailLookupActivity.responseCode == 200){
                                                MailLookupActivity.responseCode = -1;
                                                inProgress = false;
                                                runOnUiThread(progress);
                                                onBackPressed();
                                            } else if(MailLookupActivity.responseCode == 404 || MailLookupActivity.responseCode == 500){
                                                MailLookupActivity.responseCode = 0;
                                                mlau.startDialog(0, "삭제 실패", "삭제에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                inProgress = false;
                                                runOnUiThread(progress);
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

                                                runOnUiThread(runnable);
                                            }
                                        }
                                    }

                                    if(MailLookupActivity.responseCode == 0){
                                        MailLookupActivity.responseCode = -1;

                                        ControlMail_f cmf = new ControlMail_f();
                                        cmf.deleteMail(ControlLogin_f.userinfo.getNickname(), mail.getMail_id());
                                    }
                                    NewRunnable1 nr = new NewRunnable1();
                                    Thread t = new Thread(nr);
                                    t.start();
                                    break;
                                } else if(Custom_Dialog.state == 1){
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        });

        /*
        deleteMail_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // #39 쪽지 삭제하기 호출 코드
                custon_progressDialog.show();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            onBackPressed();
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

                            if (threadFlag.get())
                                runOnUiThread(runnable);
                            else {
                                i = 30;
                            }
                        }
                    }
                }

                ControlMail_f cmf = new ControlMail_f();
                cmf.deleteMail(ControlLogin_f.userinfo.getNickname(), mail.getMail_id());
                threadFlag.set(true);
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }
        });
        */
    }

    class MailLookupActivity_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(MailLookupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(MailLookupActivity.this, MailLookupActivity.class);
                startActivity(intent);
            }
        }
    }
}



/*
public void onClick(View view) {
                // #39 쪽지 삭제하기 호출 코드
                responseCode = 0;
                List<String> temp = new ArrayList<>();
                temp.add("확인");
                temp.add("취소");
                mlau.startDialog(1, "쪽지 삭제", "쪽지를 삭제하시겠습니까?", temp);

                class NewRunnable implements Runnable {
                    NewRunnable() {
                    }

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(100);
                                Log.d("Thread", "돌아가고 있어요");
                                if (Custom_Dialog.state == 0) {
                                    Custom_Dialog.state = -1;

                                    custon_progressDialog.show();

                                    Log.d("Thread", "왼쪽 버튼 눌림");
                                    ControlMail_f cmf = new ControlMail_f();
                                    cmf.deleteMail(ControlLogin_f.userinfo.getNickname(), mail.getMail_id());
//                                    final Runnable runnable = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (responseCode == 200) {
//                                                responseCode = -1;
//                                                threadFlag.set(false);
//                                                custon_progressDialog.dismiss();
//                                                onBackPressed();
//                                            } else if (responseCode == 404 || responseCode == 500) {
//                                                List<String> temp = new ArrayList<>();
//                                                temp.add("확인");
//                                                mlau.startDialog(0, "삭제 실패", "삭제에 실패했습니다.", temp);
//                                            }
//                                        }
//                                    };
//
//                                    class NeoRunnable implements Runnable {
//                                        @Override
//                                        public void run() {
//                                            for (int i = 0; i < 30; i++) {
//                                                try {
//                                                    Thread.sleep(1000);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                if (threadFlag.get())
//                                                    runOnUiThread(runnable);
//                                                else {
//                                                    i = 30;
//                                                }
//                                            }
//                                        }
//                                    }


//                                    threadFlag.set(true);
//                                    NeoRunnable nr = new NeoRunnable();
//                                    Thread t = new Thread(nr);
//                                    t.start();
                                    if(responseCode != 0){
                                            if (responseCode == 200) {
                                            responseCode = -1;
                                            threadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            onBackPressed();
                                            } else if (responseCode == 404 || responseCode == 500) {
                                            List<String> temp = new ArrayList<>();
        temp.add("확인");
        mlau.startDialog(0, "삭제 실패", "삭제에 실패했습니다.", temp);
        }
        break;
        }


        } else if (Custom_Dialog.state == 1) {
        break;
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        }
 */
/**/