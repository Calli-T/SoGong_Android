package com.example.sogong.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class RecipeAddActivity extends AppCompatActivity {
    Context context;

    EditText recipetitle;
    Spinner recipecate;
    Spinner recipespicy;
    EditText recipedescription;

    Button addingre;
    LinearLayout linearlayout1;
    LinearLayout linearlayout;
    TextView ingrename;
    TextView ingreamount;
    TextView ingreunit;
    Spinner ingrename_spinner;
    TextView ingrename_selectText;
    //ArrayList<TextView> ingrename_selectText;
    EditText ingreamount_edit;
    TextView ingreamount_text;

    String[] ingrename_str = new String[]{"아스파라거스", "닭고기", "계란", "쪽파"};
    String[] ingrecate_str = new String[]{"한식", "중식", "양식", "일식", "레시피 종류"};
    String[] spicy_int = new String[]{"0", "1", "2", "3", "4", "5", "매운맛 단계"};
    ArrayList<Integer> namespinner_id = new ArrayList<>();//동적 생성된 스피너들의 id를 관리하기 위한 리스트
    ArrayList<Integer> amountedit_id = new ArrayList<>();//동적 생성된 스피너들의 id를 관리하기 위한 리스트
    HashMap<String, String> unitmap = new HashMap<>();//재료에 맞는 단위를 위한 map
    Dialog ingreSelectDialog;

    public static int responseCode;
    public static RecipePost_f newRecipe = new RecipePost_f();//추가될 레시피게시글의 정보를 담을 객체

    List<Recipe_Ingredients> recipe_ingredients = new ArrayList<>();//레시피 재료를 담을 객체

    int ingre_cnt;//재료의 수

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);
        context = RecipeAddActivity.this;
        recipetitle = findViewById(R.id.recipetitle_text1);
        recipecate = findViewById(R.id.recipecate_spinner);
        recipespicy = findViewById(R.id.recipespicy_spinner);
        addingre = findViewById(R.id.ingreadd_btn);
        recipedescription = findViewById(R.id.recipedescription_edit);
        linearlayout = findViewById(R.id.ingre_linear);//재료추가부분의 레이아웃
        //플로팅버튼
        FloatingActionButton fab = findViewById(R.id.recipe_add_button);
        fab.setOnClickListener(new FABClickListener());//플로팅버튼 클릭 이벤트

        ingre_cnt = 0;//재료의 수
        //임시로 단위들 추가
        unitmap.put("아스파라거스", "T");
        unitmap.put("닭고기", "g");
        unitmap.put("계란", "판");
        unitmap.put("쪽파", "개");

        //레시피 종류 스피너
        SpinnerWithHintAdapter spinnerArrayAdapter1 = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrecate_str);
        spinnerArrayAdapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        recipecate.setAdapter(spinnerArrayAdapter1);
        recipecate.setSelection(spinnerArrayAdapter1.getCount());

        //매운 맛 단계 스피너
        SpinnerWithHintAdapter spinnerArrayAdapter2 = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, spicy_int);
        spinnerArrayAdapter2.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        recipespicy.setAdapter(spinnerArrayAdapter2);
        recipespicy.setSelection(spinnerArrayAdapter2.getCount());

        /*
//        //재료 및 양 초기에 하나만 띄우는 코드
//        linearlayout1 = new LinearLayout(context);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        linearlayout1.setOrientation(LinearLayout.HORIZONTAL);
//        layoutParams.setMargins(30, 0, 0, 0);
//        linearlayout1.setLayoutParams(layoutParams);
//        //재료 텍스트 생성
//        ingrename = new TextView(context);
//        ingrename.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//        ingrename.setText("재료");
//        ingrename.setTextColor(getResources().getColor(R.color.blue));
//        ingrename.setTextSize(18);
//        //양 텍스트 생성
//        ingreamount = new TextView(context);
//        ingreamount.setText("양");
//        ingreamount.setTextColor(getResources().getColor(R.color.blue));
//        ingreamount.setLayoutParams(layoutParams);
//        ingreamount.setTextSize(18);
//        //재료 선택 스피너 생성
////        ingrename_spinner = new Spinner(context);
////        SpinnerWithHintAdapter spinnerArrayAdapter = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
////        spinnerArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
////        ingrename_spinner.setAdapter(spinnerArrayAdapter);
////        ingrename_spinner.setSelection(spinnerArrayAdapter.getCount());
////        ingrename_spinner.setId(View.generateViewId());//아이디 자동  생성
////        namespinner_id.add(ingrename_spinner.getId());//생성된 아이디 리스트에 삽입
////        ingrename_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                if (position != ingrename_str.length - 1) {
////                    ingreunit.setText(unitmap.get(ingrename_spinner.getSelectedItem().toString()));
////                    ingreunit.setTextColor(getResources().getColor(R.color.blue));
////                    ingreunit.setTextSize(18);
////                    Log.d("spinner", "Spinner1: position=" + position + " id=" + id);
////                }
////            }
////            @Override
////            public void onNothingSelected(AdapterView<?> parent) {
////            }
////        });
//        //재료 선택 텍스트뷰 생성
//        ingrename_selectText = new TextView(context);
//        ingrename_selectText.setId(View.generateViewId());
//        ingrename_selectText.setCompoundDrawables(null, null, Drawable.createFromPath("@android:drawable/report"), null);
//        ingrename_selectText.setText("재료 선택");
//        ingrename_selectText.setTextColor(getResources().getColor(com.google.maps.android.rx.places.R.color.quantum_grey));
//        ingrename_selectText.setTextSize(18);
//        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());//픽셀을 dp단위로 변환
//        ingrename_selectText.setWidth(width);
//        ingrename_selectText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
//        namespinner_id.add(ingrename_selectText.getId());
//
//        //텍스트를 선택하면
//        ingrename_selectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ingreSelectDialog = new Dialog(context);
//                ingreSelectDialog.setContentView(R.layout.custom_searchablespinner);
//                ingreSelectDialog.getWindow().setLayout(650, 800);
//                ingreSelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                ingreSelectDialog.show();
//                EditText editText = ingreSelectDialog.findViewById(R.id.edit_text);
//                ListView listView = ingreSelectDialog.findViewById(R.id.list_view);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
//                listView.setAdapter(adapter);
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        adapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Log.d("position","position = "+position);
//                        ingrename_selectText.setText(adapter.getItem(position));
//                        ingrename_selectText.setTextColor(getResources().getColor(R.color.black));
//                        ingreSelectDialog.dismiss();
//                    }
//                });
//            }
//        });
//
//        ingreunit = new TextView(context);
//        ingreunit.setText("단위");
//        ingreunit.setTextColor(getResources().getColor(R.color.blue));
//        ingreunit.setTextSize(18);
//        //양 입력 텍스트 생성
//        ingreamount_edit = new EditText(context);
//        ingreamount_edit.setId(View.generateViewId());//아이디 자동  생성
//        ingreamount_edit.setBackground(Drawable.createFromPath("@android:color/transparent"));
//        ingreamount_edit.setHint("숫자 입력");
//        amountedit_id.add(ingreamount_edit.getId());//생성된 아이디 리스트에 삽입
//
//        linearlayout1.addView(ingrename);
//        linearlayout1.addView(ingrename_selectText);
//        linearlayout1.addView(ingreamount);
//        linearlayout1.addView(ingreamount_edit);
//        linearlayout1.addView(ingreunit);
//        linearlayout.addView(linearlayout1);
*/


        //버튼 클릭 시 재료 입력 ui 동적 생성
        addingre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("레시피 등록", "button click");
                ingreSelectDialog = new Dialog(context);
                ingreSelectDialog.setContentView(R.layout.custom_searchablespinner);
                ingreSelectDialog.getWindow().setLayout(750, 1000);
                ingreSelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ingreSelectDialog.show();
                EditText editText = ingreSelectDialog.findViewById(R.id.edit_text);
                ListView listView = ingreSelectDialog.findViewById(R.id.list_view);
                TextView unitText = ingreSelectDialog.findViewById(R.id.unit_text);
                EditText editText1 = ingreSelectDialog.findViewById(R.id.edit_text1);
                Button addButton = ingreSelectDialog.findViewById(R.id.ingre_add_button);
                Button cancelButton = ingreSelectDialog.findViewById(R.id.cancel_button);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {//edittext검색기능
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("position", "position = " + position);
                        editText.setText(adapter.getItem(position));
//                        ingrename_selectText.setTextColor(getResources().getColor(R.color.black));
                        unitText.setText(unitmap.get(editText.getText().toString()));
//                        ingreSelectDialog.dismiss();
                        listView.setVisibility(View.INVISIBLE);
                    }
                });
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.dynamic_ingre_item,null);
                        TextView name = view.findViewById(R.id.name);
                        TextView selectName = view.findViewById(R.id.writtenname);
                        TextView amount = view.findViewById(R.id.amount);
                        TextView editAmount = view.findViewById(R.id.writtenamount);
                        TextView unit =  view.findViewById(R.id.unit);
                        ImageButton removeButton= view.findViewById(R.id.minus_button);
                        selectName.setText(editText.getText().toString());
                        editAmount.setText(editText1.getText().toString());
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linearlayout.removeView(view);
                            }
                        });
                        linearlayout.addView(view);

                        /*linearlayout1 = new LinearLayout(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linearlayout1.setOrientation(LinearLayout.HORIZONTAL);
                        layoutParams.setMargins(30, 0, 0, 0);
                        linearlayout1.setLayoutParams(layoutParams);
                        //재료 텍스트 생성
                        ingrename = new TextView(context);
                        ingrename.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        ingrename.setText("재료");
                        ingrename.setTextColor(getResources().getColor(R.color.blue));
                        ingrename.setTextSize(18);
                        //선택한 재료 텍스트뷰 생성
                        ingrename_selectText = new TextView(context);
                        ingrename_selectText.setId(View.generateViewId());
                        ingrename_selectText.setText(editText.getText().toString());
                        ingrename_selectText.setTextColor(getResources().getColor(com.google.maps.android.rx.places.R.color.quantum_grey));
                        ingrename_selectText.setTextSize(18);
                        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());//픽셀을 dp단위로 변환
                        ingrename_selectText.setWidth(width);
                        ingrename_selectText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                        namespinner_id.add(ingrename_selectText.getId());
                        //양 텍스트 생성
                        ingreamount = new TextView(context);
                        ingreamount.setText("양");
                        ingreamount.setTextColor(getResources().getColor(R.color.blue));
                        ingreamount.setLayoutParams(layoutParams);
                        ingreamount.setTextSize(18);
                        //재료 선택 스피너 생성
//        ingrename_spinner = new Spinner(context);
//        SpinnerWithHintAdapter spinnerArrayAdapter = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
//        spinnerArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        ingrename_spinner.setAdapter(spinnerArrayAdapter);
//        ingrename_spinner.setSelection(spinnerArrayAdapter.getCount());
//        ingrename_spinner.setId(View.generateViewId());//아이디 자동  생성
//        namespinner_id.add(ingrename_spinner.getId());//생성된 아이디 리스트에 삽입
//        ingrename_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != ingrename_str.length - 1) {
//                    ingreunit.setText(unitmap.get(ingrename_spinner.getSelectedItem().toString()));
//                    ingreunit.setTextColor(getResources().getColor(R.color.blue));
//                    ingreunit.setTextSize(18);
//                    Log.d("spinner", "Spinner1: position=" + position + " id=" + id);
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

                        //입력한 양 출력
                        ingreamount_text = new TextView(context);
                        ingreamount_text.setId(View.generateViewId());
                        ingreamount_text.setText(editText1.getText().toString());
                        ingreamount_text.setTextSize(18);
                        ingreamount_text.setWidth(width);
                        ingreamount_text.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                        amountedit_id.add(ingreamount_text.getId());//생성된 아이디 리스트에 삽입

                        //재료 단위를 알맞게 출력
                        ingreunit = new TextView(context);
                        ingreunit.setText(unitText.getText().toString());
                        ingreunit.setTextColor(getResources().getColor(R.color.blue));
                        ingreunit.setTextSize(18);

//                        //양 입력 텍스트 생성
//                        ingreamount_edit = new EditText(context);
//                        ingreamount_edit.setId(View.generateViewId());//아이디 자동  생성
//                        ingreamount_edit.setBackground(Drawable.createFromPath("@android:color/transparent"));
//                        ingreamount_edit.setHint("숫자 입력");
//                        //amountedit_id.add(ingreamount_edit.getId());//생성된 아이디 리스트에 삽입

                        linearlayout1.addView(ingrename);
                        linearlayout1.addView(ingrename_selectText);
                        linearlayout1.addView(ingreamount);
                        linearlayout1.addView(ingreamount_text);
                        linearlayout1.addView(ingreunit);
                        linearlayout.addView(linearlayout1);*/
                        ingreSelectDialog.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
//                ingre_cnt++;//재료 수 증가
//
//                linearlayout1 = new LinearLayout(context);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                linearlayout1.setOrientation(LinearLayout.HORIZONTAL);
//                layoutParams.setMargins(30, 0, 0, 0);
//                linearlayout1.setLayoutParams(layoutParams);
//                //재료 텍스트 생성
//                ingrename = new TextView(context);
//                ingrename.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                ingrename.setText("재료");
//                ingrename.setTextColor(getResources().getColor(R.color.blue));
//                ingrename.setTextSize(18);
//                //양 텍스트 생성
//                ingreamount = new TextView(context);
//                ingreamount.setText("양");
//                ingreamount.setTextColor(getResources().getColor(R.color.blue));
//                ingreamount.setLayoutParams(layoutParams);
//                ingreamount.setTextSize(18);
//                //재료 선택 스피너 생성
////        ingrename_spinner = new Spinner(context);
////        SpinnerWithHintAdapter spinnerArrayAdapter = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
////        spinnerArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
////        ingrename_spinner.setAdapter(spinnerArrayAdapter);
////        ingrename_spinner.setSelection(spinnerArrayAdapter.getCount());
////        ingrename_spinner.setId(View.generateViewId());//아이디 자동  생성
////        namespinner_id.add(ingrename_spinner.getId());//생성된 아이디 리스트에 삽입
////        ingrename_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                if (position != ingrename_str.length - 1) {
////                    ingreunit.setText(unitmap.get(ingrename_spinner.getSelectedItem().toString()));
////                    ingreunit.setTextColor(getResources().getColor(R.color.blue));
////                    ingreunit.setTextSize(18);
////                    Log.d("spinner", "Spinner1: position=" + position + " id=" + id);
////                }
////            }
////            @Override
////            public void onNothingSelected(AdapterView<?> parent) {
////            }
////        });
//                //재료 선택 텍스트뷰 생성
//                ingrename_selectText = new TextView(context);
//                ingrename_selectText.setId(View.generateViewId());
//                ingrename_selectText.setCompoundDrawables(null, null, Drawable.createFromPath("@android:drawable/report"), null);
//                ingrename_selectText.setText("재료 선택");
//                ingrename_selectText.setTextColor(getResources().getColor(com.google.maps.android.rx.places.R.color.quantum_grey));
//                ingrename_selectText.setTextSize(18);
//                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());//픽셀을 dp단위로 변환
//                ingrename_selectText.setWidth(width);
//                ingrename_selectText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
//                namespinner_id.add(ingrename_selectText.getId());
//
//                //텍스트를 선택하면
//                ingrename_selectText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ingreSelectDialog = new Dialog(context);
//                        ingreSelectDialog.setContentView(R.layout.custom_searchablespinner);
//                        ingreSelectDialog.getWindow().setLayout(650, 800);
//                        ingreSelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        ingreSelectDialog.show();
//                        EditText editText = ingreSelectDialog.findViewById(R.id.edit_text);
//                        ListView listView = ingreSelectDialog.findViewById(R.id.list_view);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
//                        listView.setAdapter(adapter);
//                        editText.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                adapter.getFilter().filter(s);
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//
//                            }
//                        });
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Log.d("position","position = "+position);
//                                ingrename_selectText.setText(adapter.getItem(position));
//                                ingrename_selectText.setTextColor(getResources().getColor(R.color.black));
//                                ingreSelectDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//
//                ingreunit = new TextView(context);
//                ingreunit.setText("단위");
//                ingreunit.setTextColor(getResources().getColor(R.color.blue));
//                ingreunit.setTextSize(18);
//                //양 입력 텍스트 생성
//                ingreamount_edit = new EditText(context);
//                ingreamount_edit.setId(View.generateViewId());//아이디 자동  생성
//                ingreamount_edit.setBackground(Drawable.createFromPath("@android:color/transparent"));
//                ingreamount_edit.setHint("숫자 입력");
//                amountedit_id.add(ingreamount_edit.getId());//생성된 아이디 리스트에 삽입
//
//                linearlayout1.addView(ingrename);
//                linearlayout1.addView(ingrename_selectText);
//                linearlayout1.addView(ingreamount);
//                linearlayout1.addView(ingreamount_edit);
//                linearlayout1.addView(ingreunit);
//                linearlayout.addView(linearlayout1);
            }
        });
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간

            newRecipe.setNickname(ControlLogin_f.userinfo.getNickname());
            newRecipe.setTitle(recipetitle.getText().toString());
            newRecipe.setCategory(recipecate.getSelectedItem().toString());
            newRecipe.setDegree_of_spicy(Integer.parseInt(recipespicy.getSelectedItem().toString()));
            newRecipe.setDescription(recipedescription.getText().toString());
            for (int i = 0; i<linearlayout.getChildCount(); i++){
                View tempview = linearlayout.getChildAt(i);
                TextView nameTemp = tempview.findViewById(R.id.writtenname);
                TextView amountTemp = tempview.findViewById(R.id.writtenamount);
                String temp_ingrename = nameTemp.getText().toString();
                float temp_amount = Float.parseFloat(amountTemp.getText().toString());

                Recipe_Ingredients tempingredients = new Recipe_Ingredients();
                tempingredients.setName(temp_ingrename);
                tempingredients.setAmount(temp_amount);
                tempingredients.setUnit(unitmap.get(temp_ingrename));

                recipe_ingredients.add(tempingredients);
            }
            //Arraylist사이즈만큼 반복해서 재료와 양의 리스트 만들기
//            for (int i = 0; i < namespinner_id.size(); i++) {
//                //Spinner spinnertemp;
//                TextView texttemp;
//                EditText editTexttemp;
//                Recipe_Ingredients tempingredients = new Recipe_Ingredients();
//                //spinnertemp = findViewById(namespinner_id.get(i));
//                texttemp = findViewById(namespinner_id.get(i));
//                editTexttemp = findViewById(amountedit_id.get(i));
//                //String temp_ingrename = spinnertemp.getSelectedItem().toString();
//                String temp_ingrename = texttemp.getText().toString();
//                float temp_amount = Float.parseFloat(editTexttemp.getText().toString());
//                tempingredients.setName(temp_ingrename);
//                tempingredients.setAmount(temp_amount);
//                tempingredients.setUnit(unitmap.get(temp_ingrename));
//                recipe_ingredients.add(tempingredients);
//            }
            newRecipe.setRecipe_Ingredients(recipe_ingredients);

            Log.d("recipe", newRecipe.toString());
            //newRecipe로 게시글 등록 함수에 넣으면 됨
        }
    }

    //spinner hint를 위한 클래스 재정의
    public class SpinnerWithHintAdapter extends ArrayAdapter<String> {

        Context context;

        public SpinnerWithHintAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView tvSpinner = (TextView) view;
            if (position == getCount()) {
                // setting the hint text color primary color
                tvSpinner.setTextColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_grey
                ));
            } else {
                tvSpinner.setTextColor(Color.BLACK);
            }
            return view;
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}
