package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    TextView mailTitle;
    TextView mailAuthor;
    TextView mailDate;
    TextView mailDescription;
    Button deleteMail_Button;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그

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

        deleteMail_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // #39 쪽지 삭제하기 호출 코드

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

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }
        });

    }


}
