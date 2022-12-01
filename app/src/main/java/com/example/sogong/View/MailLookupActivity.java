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

        //넘겨온 값으로 값 입력
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
        if(mail == null){
            mlau.startDialog(0,"쪽지 조회 실패","쪽지를 가져오는데 실패했습니다.",new ArrayList<>(Arrays.asList("확인")));
        }
        //삭제 버튼 클릭
        deleteMail_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제 여부 되묻기
                mlau.startDialog(1, "쪽지 삭제", "쪽지를 삭제하시겠습니까?", new ArrayList<>(Arrays.asList("삭제", "취소")));

                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(100);
                                //확인을 누른 경우
                                if (Custom_Dialog.state == 0) {
                                    Custom_Dialog.state = -1;
                                    inProgress = true;

                                    final Runnable progress = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (inProgress) {//isProgress가 참이면 로딩창 띄우고 거짓이면 로딩창 사라진다.
                                                custon_progressDialog.show();
                                            } else custon_progressDialog.dismiss();
                                        }
                                    };
                                    runOnUiThread(progress);//로딩창 띄우기
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (MailLookupActivity.responseCode == 200) {
                                                MailLookupActivity.responseCode = -1;
                                                inProgress = false;
                                                threadFlag.set(false);
                                                runOnUiThread(progress);
                                                onBackPressed();
                                            } else if (MailLookupActivity.responseCode == 404 || MailLookupActivity.responseCode == 500) {
                                                MailLookupActivity.responseCode = 0;
                                                mlau.startDialog(0, "삭제 실패", "삭제에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                inProgress = false;
                                                threadFlag.set(false);
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
                                                if (threadFlag.get()) {
                                                    runOnUiThread(runnable);
                                                } else {
                                                    i = 30;
                                                }
                                            }
                                        }
                                    }

                                    MailLookupActivity.responseCode = -1;
                                    ControlMail_f cmf = new ControlMail_f();
                                    cmf.deleteMail(ControlLogin_f.userinfo.getNickname(), mail.getMail_id());
                                    NewRunnable1 nr = new NewRunnable1();
                                    Thread t = new Thread(nr);
                                    threadFlag.set(true);
                                    //삭제된 것을 확인하는 쓰레드
                                    t.start();
                                    break;
                                } else if (Custom_Dialog.state == 1) {
                                    //취소를 누른 경우 작동하지 않음
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
                //되묻는 다이얼로그에서의 버튼 클릭을 기다리는 쓰레드
                t.start();
            }
        });
    }

    class MailLookupActivity_UI implements Control {
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