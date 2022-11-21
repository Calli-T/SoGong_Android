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

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailBoxActivity extends AppCompatActivity {

    String[] pagenum;
    public static int totalpage;
    public static List<Mail> maillist;
    public static int responseCode = 0;
    public MailAdapter mailAdapter;
    public RecyclerView mailRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        FloatingActionButton fab = findViewById(R.id.mail_add_button);
        fab.setOnClickListener(new MailBoxActivity.FABClickListener());

        mailRecyclerView = (RecyclerView) findViewById(R.id.mail_recyclerview);

        mailAdapter = new MailAdapter();

        mailRecyclerView.setAdapter(mailAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MailBoxActivity.this);
        mailRecyclerView.setLayoutManager(layoutManager);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    mailAdapter.setRecipeList(maillist);

                    pagenum = new String[totalpage];
                    for (int i = 1; i <= totalpage; i++) {
                        pagenum[i - 1] = String.valueOf(i);
                    }
                    //페이지 수 스피너 설정
                    Spinner pagespinner = findViewById(R.id.recipe_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MailBoxActivity.this, android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pagespinner.setAdapter(adapter1);
                    pagespinner.setPrompt("이동할 페이지");

                    pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
//                            cmlf.lookupMailList(position+1,ControlLogin_f.userinfo.getNickname(););
//                            //업데이트된 레시피 리스트로 전환
//                            mailAdapter.setRecipeList(maillist);
//                            //레시피 리사이클러뷰 클릭 이벤트
//                            //메일 리사이클러뷰 클릭 이벤트
//                            mailAdapter.setOnItemClickListener(new MailAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(int position, String data) {
//                            Intent intent = new Intent(MailBoxActivity.this, MailLookupActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            intent.putExtra("mail", maillist.get(position));
//                            startActivity(intent);
//                        }
//                    });
                            Log.d("recipefragment", "page spinner " + position + " 클릭");
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
            Intent intent = new Intent(MailBoxActivity.this, RecipeAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

}
