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
import android.widget.EditText;
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

public class MailSendActivity extends AppCompatActivity {

    public static int responseCode;

    EditText mailReceiver;
    EditText mailTitle;
    EditText mailDescription;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private MailSendActivity_UI mui = new MailSendActivity_UI();
    Custon_ProgressDialog custon_progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        String receiver = getIntent().getStringExtra("mail_receiver");

        mailReceiver = findViewById(R.id.mailreceiver_edit);
        mailTitle = findViewById(R.id.mailtitle_edit);
        mailDescription = findViewById(R.id.maildescription_edit);

        FloatingActionButton fab = findViewById(R.id.mail_send_button);
        fab.setOnClickListener(new FABClickListener());
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        if (receiver != null) {
            mailReceiver.setText(receiver);
        }
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            custon_progressDialog.show();
            // FAB Click 이벤트 처리 구간
            Mail mail = new Mail(0, ControlLogin_f.userinfo.getNickname(), mailReceiver.getText().toString(), mailTitle.getText().toString(), mailDescription.getText().toString(), "", false, false);
            threadFlag.set(true);
            // #38 쪽지 보내기 호출 코드
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        responseCode = -1;
                        threadFlag.set(false);
                        custon_progressDialog.dismiss();
                        Log.d("send","성공적으로 보냄");
                        finish();
                    } else if (responseCode == 400 || responseCode == 500) {
                        responseCode = -1;
                        threadFlag.set(false);
                        custon_progressDialog.dismiss();
                        List<String> temp = new ArrayList<>();
                        temp.add("확인");
                        mui.startDialog(0, "전송 실패", "쪽지 전송에 실패했습니다.", temp);
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
            cmf.sendMail(mail);
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();
        }
    }
    class MailSendActivity_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(MailSendActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }
        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(MailSendActivity.this, MailSendActivity.class);
                startActivity(intent);
            }
        }
    }
}
