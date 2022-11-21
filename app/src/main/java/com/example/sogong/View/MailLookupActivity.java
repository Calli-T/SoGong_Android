package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailLookupActivity extends AppCompatActivity {


    public static int responseCode = 0;
    TextView mailTitle;
    TextView mailAuthor;
    TextView mailDate;
    TextView mailDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookupmail);
        Mail mail = getIntent().getParcelableExtra("mail");
        mailTitle = findViewById(R.id.mailtitle_text1);
        mailAuthor = findViewById(R.id.mailauthor_text1);
        mailDate = findViewById(R.id.maildate_text1);
        mailDescription = findViewById(R.id.maildescription_text);

        mailTitle.setText(mail.getTitle());
        mailAuthor.setText(mail.getNickname());
        String date = mail.getSend_time().split("T")[0];
        mailDate.setText(date);
        mailDescription.setText(mail.getContents());


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


}
