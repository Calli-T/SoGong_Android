package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MailBoxActivity extends AppCompatActivity {

    TextView send;
    TextView receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        send = findViewById(R.id.sendmail_text);
        receive = findViewById(R.id.receivemail_text);
        FloatingActionButton fab = findViewById(R.id.mail_add_button);
        fab.setOnClickListener(new MailBoxActivity.FABClickListener());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    class FABClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intent = new Intent(MailBoxActivity.this, RecipeAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

}
