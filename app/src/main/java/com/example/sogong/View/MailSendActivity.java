package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailSendActivity extends AppCompatActivity {

    public static int responseCode = 0;
    EditText mailReceiver;
    EditText mailTitle;
    EditText mailDescription;


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

        if (receiver != null) {
            mailReceiver.setText(receiver);
        }


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


                    i = 30;

                }
            }
        }
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Mail mail = new Mail(0, ControlLogin_f.userinfo.getNickname(), mailReceiver.getText().toString(), mailTitle.getText().toString(), mailDescription.getText().toString(), "", false, false);
            /* #38 쪽지 보내기 */
            //cmf.sendMail(mail);
            onBackPressed();
        }
    }

}
