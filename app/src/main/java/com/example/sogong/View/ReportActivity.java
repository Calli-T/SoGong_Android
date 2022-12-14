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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlRefrigerator_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.Report;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReportActivity extends AppCompatActivity {
    EditText reportReason;
    Button report_btn;
    Button cancel_btn;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    public static int responseCode; // 응답 코드
    Custon_ProgressDialog custon_progressDialog;
    ControlReport_f crf = new ControlReport_f();
    Report_UI ru = new Report_UI();
    ImageButton back_button;

    Boolean isProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        int comment_id = getIntent().getExtras().getInt("comment_id");
        int recipe_id = getIntent().getExtras().getInt("recipe_id");
        int photo_id = getIntent().getExtras().getInt("photo_id");
        int posttype = getIntent().getExtras().getInt("report_post_type");
        reportReason = findViewById(R.id.report_edit);
        report_btn = findViewById(R.id.report_button);
        cancel_btn = findViewById(R.id.cancel_button);
        responseCode = 0;
        threadFlag.set(true);

        //뒤로가기버튼
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        //신고창 null 처리하기

        //신고 버튼
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 빈 신고에 관하여
                if (reportReason.getText().toString().equals("")) {
                    ru.startDialog(0, "양식 오류", "신고 내용을 입력해주세요.", new ArrayList<>(Arrays.asList("확인")));
                } else {
                    if (posttype == -1) {
                        ru.startDialog(1, "신고 확인", "댓글을 신고하시겠습니까?", new ArrayList<>(Arrays.asList("신고", "취소")));
                    } else
                        ru.startDialog(1, "신고 확인", "게시글을 신고하시겠습니까?", new ArrayList<>(Arrays.asList("신고", "취소")));
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
                                            }
                                        };

                                        switch (posttype) {
                                            //댓글 신고
                                            case -1:
                                                final Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (responseCode == 200) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            onBackPressed();
                                                        } else if (responseCode == 500) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "댓글 신고", "댓글 신고를 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                        } else if (responseCode == 502) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                                        }
                                                    }
                                                };

                                                class NewRunnable4 implements Runnable {
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
                                                Report commentReport = new Report(ControlLogin_f.userinfo.getNickname(), reportReason.getText().toString(), comment_id, -1);
                                                crf.reportComment(commentReport);
                                                threadFlag.set(true);
                                                runOnUiThread(progress);
                                                NewRunnable4 nr4 = new NewRunnable4();
                                                Thread t = new Thread(nr4);
                                                t.start();
                                                //cref.reportComment(reportInfo);
                                                Log.d("report", "댓글 신고하러 왔어요");
                                                break;
                                            case 1:
                                                final Runnable runnable1 = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (responseCode == 200) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            onBackPressed();
                                                        } else if (responseCode == 500) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "등록 실패", "신고 등록에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                        } else if (responseCode == 502) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                                                            if (threadFlag.get())
                                                                runOnUiThread(runnable1);
                                                            else {
                                                                i = 30;
                                                            }
                                                        }
                                                    }
                                                }
                                                Report recipeReport = new Report(ControlLogin_f.userinfo.getNickname(), reportReason.getText().toString(), recipe_id, 1);
                                                crf.reportPost(recipeReport);
                                                threadFlag.set(true);
                                                runOnUiThread(progress);
                                                NewRunnable1 nr1 = new NewRunnable1();
                                                Thread t1 = new Thread(nr1);
                                                t1.start();

                                                Log.d("report", "레시피 신고하러 왔어요");
                                                break;
                                            case 2:
                                                Log.d("report", "사진 신고하러 왔어요");
                                                /* #22 요리 사진 게시글 신고 */
                                                final Runnable runnable2 = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (responseCode == 200) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            onBackPressed();
                                                        } else if (responseCode == 500) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "서버 오류", "신고 정보 등록을 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                        } else if (responseCode == 502) {
                                                            responseCode = -1;
                                                            threadFlag.set(false);
                                                            isProgress = false;
                                                            runOnUiThread(progress);
                                                            ru.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));

                                                        }
                                                    }
                                                };

                                                class NewRunnable2 implements Runnable {
                                                    @Override
                                                    public void run() {
                                                        for (int i = 0; i < 30; i++) {
                                                            try {
                                                                Thread.sleep(1000);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            if (threadFlag.get())
                                                                runOnUiThread(runnable2);
                                                            else {
                                                                i = 30;
                                                            }
                                                        }
                                                    }
                                                }
                                                Report photoReport = new Report(ControlLogin_f.userinfo.getNickname(), reportReason.getText().toString(), 1, 2);
                                                crf.reportPost(photoReport);
                                                threadFlag.set(true);
                                                isProgress = true;
                                                runOnUiThread(progress);
                                                NewRunnable2 nr2 = new NewRunnable2();
                                                Thread t2 = new Thread(nr2);
                                                t2.start();
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
            }
        });

        // 취소 버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class Report_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(ReportActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {

        }
    }
}
