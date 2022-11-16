package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button login_button, join_button;
    CheckBox checkbox;
    TextInputLayout textInputLayout2;

    public static int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI controller
        Login_UI lu = new Login_UI();

        // 사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        login_button = findViewById(R.id.login_button);
        join_button = findViewById(R.id.join_button);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        checkbox = findViewById(R.id.checkBox);

        // 로그인 버튼 이벤트 추가
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Custom_Dialog customDialog = new Custom_Dialog(LoginActivity.this);
                //customDialog.callFunction("서버 오류","로그인에 실패했습니다.",1);
                lu.startToast("로그인 중");
                String id = userid_et.getText().toString();
                String pw = passwd_et.getText().toString();
                boolean auto_login = checkbox.isChecked();

                // 이하 3개 문단의 코드는 Retrofit의 비동기와 UI Thread의 동기화를 위한 코드이다
                /*
                컨트롤러에서 API호출을 하는동안
                대기하다 컨트롤러에서 호출을 성공하여 activity의 static field를 채워주면
                activity를 전환하며, 해당 field를 되돌려주는 코드이다.
                */
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = 0;
                            //Toast_Nomal("로그인 성공");

                            lu.changePage(0);
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(intent);
                        } else if (responseCode == 400) { // custom dialog랑 toast 및 control 구현해둘것
                            responseCode = 0;
                            lu.startToast("아이디 또는 비밀번호를 잘못 입력했습니다.");
                        } else if (responseCode == 500) {
                            responseCode = 0;
                        }
                    }
                };

                class NewRunnable implements Runnable {
                    @Override
                    public void run() {
                        int i = 30;
                        while (i > 0) {
                            i--;

                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(runnable);
                        }
                    }
                }

                if (responseCode != -1) {
                    ControlLogin_f clf = new ControlLogin_f();
                    clf.login(id, pw, auto_login);
                    responseCode = -1;
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lu.changePage(1);
            }
        });
    }

    class Login_UI implements Control {

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
            Custom_Dialog cd = new Custom_Dialog(LoginActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                // 회원가입에서 요청한 이메일 인증
                EmailVerificationActivity.destination = 0;

                Intent intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }

    }

}

// 위는 UI controler, 아래는 Dialog코드
                /*
                ArrayList<String> temp = new ArrayList<>();
                temp.add("확인");
                luc.startDialog(0, "TestT", "TestMSG", temp);
                */

// Toast 예제 코드
//luc.startToast("테스트용");

// response 500에 있던 코드들
/*
                            Custom_Dialog customDialog = new Custom_Dialog(LoginActivity.this);
                            customDialog.callFunction("서버 오류","로그인에 실패했습니다.",0);
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("서버 오류");
                            builder.setMessage("로그인에 실패했습니다.");
                            builder.setPositiveButton("확인", null);
                            builder.create().show();
                             */

/*
public void Toast_Nomal(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.toast_layout));
        TextView toast_textview  = layout.findViewById(R.id.toast_textview);
        toast_textview.setText(String.valueOf(message));
        Toast toast = new Toast(getApplicationContext());
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO 메시지가 표시되는 위치지정 (가운데 표시)
        //toast.setGravity(Gravity.TOP, 0, 0); //TODO 메시지가 표시되는 위치지정 (상단 표시)
        //toast.setGravity(Gravity.BOTTOM, 0, 0); //TODO 메시지가 표시되는 위치지정 (하단 표시)
        toast.setDuration(Toast.LENGTH_SHORT); //메시지 표시 시간
        toast.setView(layout);
        toast.show();
    }
 */

//
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String strTime = sdf.format(cal.getTime());

                clockTextView = findViewById(R.id.clock);
                clockTextView.setText(strTime);
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                while(true){

                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    runOnUiThread(runnable);
                }
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

/*
    public void login() {
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<User> call = sv.Login(new User(null, userid_et.getText().toString(), passwd_et.getText().toString(), null, false));//, 0));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // 반응 제대로 옴
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else
                        Log.d("empty_response", "with login");

                    //Toast.makeText(LoginActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    // 안옴
                    String result = response.toString();
                    String[] results = result.split(",");
                    //Log.d("sex", results[1]);
                    Toast.makeText(LoginActivity.this, results[1], Toast.LENGTH_SHORT).show();
                }
            }
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                Custom_Dialog customDialog = new Custom_Dialog(LoginActivity.this);

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction();
            }
        });
//        login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
//                Call<User> call = sv.Login(testUser);//, 0));
////
//                call.enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//                        if (response.isSuccessful()) {
////                            //TextView tv = findViewById(R.id.muyaho);
////                            //tv.setText(response.body().toString());
//                            if (response.body() != null)
//                                Log.d("성공", response.body().toString());
//                            else
//                                Log.d("실패", "실패ㅜㅜ");
//
//                            Toast.makeText(LoginActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            String result = response.toString();
//                            String[] results = result.split(",");
//                            Log.d("sex", results[1]);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//                        Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
 */

/*
class LoginTask extends AsyncTask<String, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    Log.d(TAG, "onPreExecute");
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    Log.d(TAG, "onPostExecute, " + result);

                    if (result.equals("success")) {
// 결과값이 success 이면
// 토스트 메시지를 뿌리고
// userid 값을 가지고 ListActivity 로 이동
                        Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, BoardListActivity.class);
                        intent.putExtra("userid", userid_et.getText().toString());
                        startActivity(intent);
                    } else if (result.equals("fail")) {
                        textInputLayout2.setError("로그인실패");
                        Toast.makeText(LoginActivity.this, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected String doInBackground(String... params) {

                    String userid = params[0];
                    String passwd = params[1];

                    //final String[] result = {""};

                    RetrofitService retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
                    Call<User> call = retrofitService.getLogin(userid);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            String result = "";
                            if (response.isSuccessful()) {
                                User user = response.body();
                                Log.d("성공", user.getUserId());
                                if (passwd.equals(user.getPassword())) {
                                    Log.d("성공", "비밀번호 일치");
                                    result = "success";
                                } else {
                                    Log.d("실패", "비밀번호 불일치");
                                    result += "fail";

                                }
                                ;

                            } else {
                                Log.d("실패", "없는 사용자 아이디 입니다.");
                                result += "fail";
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                    String result = "success";
                    return result;
                }
            }
 */
/*
// 조인 버튼 이벤트 추가
                join_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                        //startActivity(intent);
                    }
                });
 */