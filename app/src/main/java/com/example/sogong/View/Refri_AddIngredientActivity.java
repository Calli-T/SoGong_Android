package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlRefrigerator_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Refri_AddIngredientActivity extends AppCompatActivity {
    public static int responseCode; // 응답 코드
    Spinner ingreName;
    EditText ingreAmount;
    Spinner ingreUnit;
    EditText ingreExpiredate;

    private AtomicBoolean threadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    private AtomicBoolean editthreadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    Custon_ProgressDialog custon_progressDialog;
    int type;
    Refrigerator ingredients;
    RefriAdd_UI rau = new RefriAdd_UI();
    Boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingredient);
        ingredients = getIntent().getParcelableExtra("ingredient");
        type = getIntent().getIntExtra("edit", 0);

        // 사용할 컴포넌트 등록
        ingreName = findViewById(R.id.ingretitle_spinner);
        ingreAmount = findViewById(R.id.ingreamount_edit);
        ingreUnit = findViewById(R.id.ingre_unit_spinner);
        ingreExpiredate = findViewById(R.id.ingre_expiredate_edit);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(Refri_AddIngredientActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ingredients));
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingreName.setAdapter(nameAdapter);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(Refri_AddIngredientActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.unit));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingreUnit.setAdapter(unitAdapter);

        FloatingActionButton fab = findViewById(R.id.recipe_add_button);
        fab.setOnClickListener(new FABClickListener());

        ArrayList<String> ingreName_str = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ingredients)));
        ArrayList<String> ingreUnit_str = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.unit)));
        if (type == 1) {
            ingreName.setSelection(ingreName_str.indexOf(ingredients.getName()));
            ingreUnit.setSelection(ingreUnit_str.indexOf(ingredients.getUnit()));
            ingreAmount.setText(Float.toString(ingredients.getAmmount()));
            if (!ingredients.getExpiry_date().equals("")) {
                ingreExpiredate.setText(ingredients.getExpiry_date());
            }
            ingreName.setEnabled(false);
        }
    }

    //재료 추가
    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            isExist = false;
            if (RefrigeratorActivity.ingreList != null) {
                for (int i = 0; i < RefrigeratorActivity.ingreList.size(); i++) {
                    Log.d("ingrelist", RefrigeratorActivity.ingreList.toString());
                    Refrigerator temp = RefrigeratorActivity.ingreList.get(i);
                    if (temp.getName().equals(ingreName.getSelectedItem().toString())) {
                        isExist = true;
                    }
                }
            }
            if (type == 0) {
                if (isExist) {
                    rau.startToast("이미 있는 재료입니다.");
                } else {
                    Log.d("재료 등록", "1");
                    custon_progressDialog = new Custon_ProgressDialog(Refri_AddIngredientActivity.this);
                    custon_progressDialog.setCanceledOnTouchOutside(false);
                    custon_progressDialog.show();
                    if (!ingreExpiredate.getText().toString().equals("")) {
                        Log.d("재료 등록", "2");
                        if ((!ingreExpiredate.getText().toString().matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")) || (ingreAmount.getText().toString().equals(""))) {
                            Log.d("재료 등록", "3");
                            rau.startToast("형식에 맞지 않은 정보가 있습니다.");
                            custon_progressDialog.dismiss();
                        } else {
                            Log.d("재료 등록", "4");
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (responseCode == 200) {
                                        Log.d("재료 등록", "성공");
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        onBackPressed();
                                    } else if (responseCode == 400) {
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        rau.startDialog(0, "요청 실패", "재료 추가 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                    } else if (responseCode == 500) {
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        rau.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                                        Log.d("재료 추가", "responsecode = " + responseCode);

                                        if (threadFlag.get())
                                            runOnUiThread(runnable);
                                        else {
                                            i = 30;
                                        }
                                    }
                                }
                            }
                            threadFlag.set(true);
                            NewRunnable nr = new NewRunnable();
                            Thread t = new Thread(nr);
                            ControlRefrigerator_f crf = new ControlRefrigerator_f();
                            Refrigerator ingredient = new Refrigerator(0, ingreName.getSelectedItem().toString(), ControlLogin_f.userinfo.getNickname(), ingreUnit.getSelectedItem().toString(), Float.parseFloat(ingreAmount.getText().toString()), ingreExpiredate.getText().toString());
                            crf.addRefrigerator(ingredient);
                            t.start();
                        }

                    } else if (ingreExpiredate.getText().toString().equals("")) {
                        Log.d("재료 등록", "5");
                        if (ingreAmount.getText().toString().equals("")) {
                            Log.d("재료 등록", "6");
                            rau.startToast("형식에 맞지 않은 정보가 있습니다.");
                            custon_progressDialog.dismiss();
                        } else {
                            Log.d("재료 등록", "7");
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (responseCode == 200) {
                                        Log.d("재료 등록", "성공");
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        onBackPressed();
                                    } else if (responseCode == 400) {
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        rau.startDialog(0, "요청 실패", "재료 추가 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                    } else if (responseCode == 500) {
                                        responseCode = -1;
                                        threadFlag.set(false);
                                        custon_progressDialog.dismiss();
                                        rau.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                                        Log.d("재료 추가", "responsecode = " + responseCode);

                                        if (threadFlag.get())
                                            runOnUiThread(runnable);
                                        else {
                                            i = 30;
                                        }
                                    }
                                }
                            }
                            threadFlag.set(true);
                            NewRunnable nr = new NewRunnable();
                            Thread t = new Thread(nr);
                            ControlRefrigerator_f crf = new ControlRefrigerator_f();
                            Refrigerator ingredient = new Refrigerator(0, ingreName.getSelectedItem().toString(), ControlLogin_f.userinfo.getNickname(), ingreUnit.getSelectedItem().toString(), Float.parseFloat(ingreAmount.getText().toString()), ingreExpiredate.getText().toString());
                            crf.addRefrigerator(ingredient);
                            t.start();
                        }
                    }
                }
            } else if (type == 1) {
                custon_progressDialog = new Custon_ProgressDialog(Refri_AddIngredientActivity.this);
                custon_progressDialog.setCanceledOnTouchOutside(false);
                if (ingreAmount.getText().toString().equals("")) {
                    rau.startDialog(0, "유효하지 않음", "입력 정보가 유효하지 않습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else {
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 200) {
                                Log.d("재료 변경", "성공");
                                responseCode = -1;
                                editthreadFlag.set(false);
                                custon_progressDialog.dismiss();
                                onBackPressed();
                            } else if (responseCode == 406) {
                                responseCode = -1;
                                editthreadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "변경 실패", "재료 변경에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                            } else if (responseCode == 500) {
                                responseCode = -1;
                                editthreadFlag.set(false);
                                custon_progressDialog.dismiss();
                                rau.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                                Log.d("재료 변경", "responsecode = " + responseCode);

                                if (editthreadFlag.get())
                                    runOnUiThread(runnable);
                                else {
                                    i = 30;
                                }
                            }
                        }
                    }
                    editthreadFlag.set(true);
                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    custon_progressDialog.show();
                    ControlRefrigerator_f crf = new ControlRefrigerator_f();
                    Refrigerator ingredient = new Refrigerator(ingredients.getRefrigerator_id(), ingreName.getSelectedItem().toString(), ControlLogin_f.userinfo.getNickname(), ingreUnit.getSelectedItem().toString(), Float.parseFloat(ingreAmount.getText().toString()), ingreExpiredate.getText().toString());
                    crf.editRefrigerator(ingredient);
                    t.start();
                }
            }
        }
    }

    class RefriAdd_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(Refri_AddIngredientActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {

        }
    }
}
