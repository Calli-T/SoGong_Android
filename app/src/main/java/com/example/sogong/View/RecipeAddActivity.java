package com.example.sogong.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    EditText ingreamount_edit;
    String[] ingrename_str = new String[]{"아스파라거스", "닭고기", "계란", "재료 이름"};
    String[] ingrecate_str = new String[]{"한식","중식","양식","일식","레시피 종류"};
    String[] spicy_int= new String[]{"1","2","3","4","5","매운맛 단계"};
    ArrayList<Integer> namespinner_id = new ArrayList<>();//동적 생성된 스피너들의 id를 관리하기 위한 리스트
    ArrayList<Integer> amountedit_id = new ArrayList<>();//동적 생성된 스피너들의 id를 관리하기 위한 리스트
    HashMap<String, String> unitmap = new HashMap<>();//재료에 맞는 단위를 위한 map


    public static int responseCode;
    public static RecipePost_f newRecipe = new RecipePost_f();//추가될 레시피게시글의 정보를 담을 객체

    List<Recipe_Ingredients> recipe_ingredients = new ArrayList<>();//레시피 재료를 담을 객체

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

        //임시로 단위들 추가
        unitmap.put("아스파라거스","T");
        unitmap.put("닭고기","g");
        unitmap.put("계란","판");

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

        //재료 및 양 초기에 하나만 띄우는 코드
        linearlayout1 = new LinearLayout(context);
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
        //양 텍스트 생성
        ingreamount = new TextView(context);
        ingreamount.setText("양");
        ingreamount.setTextColor(getResources().getColor(R.color.blue));
        ingreamount.setTextSize(18);
        //재료 선택 스피너 생성
        ingrename_spinner = new Spinner(context);
        SpinnerWithHintAdapter spinnerArrayAdapter = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
        spinnerArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        ingrename_spinner.setAdapter(spinnerArrayAdapter);
        ingrename_spinner.setSelection(spinnerArrayAdapter.getCount());
        ingrename_spinner.setId(View.generateViewId());//아이디 자동  생성
        namespinner_id.add(ingrename_spinner.getId());//생성된 아이디 리스트에 삽입
        ingrename_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != ingrename_str.length - 1) {
                    ingreunit.setText(unitmap.get(ingrename_spinner.getSelectedItem().toString()));
                    ingreunit.setTextColor(getResources().getColor(R.color.blue));
                    ingreunit.setTextSize(18);
                    Log.d("spinner", "Spinner1: position=" + position + " id=" + id);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ingreunit = new TextView(context);
        ingreunit.setText("단위");
        ingreunit.setTextColor(getResources().getColor(R.color.blue));
        ingreunit.setTextSize(18);
        //양 입력 텍스트 생성
        ingreamount_edit = new EditText(context);
        ingreamount_edit.setId(View.generateViewId());//아이디 자동  생성
        ingreamount_edit.setBackground(Drawable.createFromPath("@android:color/transparent"));
        ingreamount_edit.setHint("숫자 입력");
        amountedit_id.add(ingreamount_edit.getId());//생성된 아이디 리스트에 삽입

        linearlayout1.addView(ingrename);
        linearlayout1.addView(ingrename_spinner);
        linearlayout1.addView(ingreamount);
        linearlayout1.addView(ingreamount_edit);
        linearlayout1.addView(ingreunit);
        linearlayout.addView(linearlayout1);
        //버튼 클릭 시 재료 입력 ui 동적 생성
        addingre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("레시피 등록", "button click");
                linearlayout1 = new LinearLayout(context);
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
                //양 텍스트 생성
                ingreamount = new TextView(context);
                ingreamount.setText("양");
                ingreamount.setTextColor(getResources().getColor(R.color.blue));
                ingreamount.setTextSize(18);
                //재료 선택 스피너 생성
                ingrename_spinner = new Spinner(context);
                SpinnerWithHintAdapter spinnerArrayAdapter = new SpinnerWithHintAdapter(context, android.R.layout.simple_spinner_dropdown_item, ingrename_str);
                spinnerArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                ingrename_spinner.setAdapter(spinnerArrayAdapter);
                ingrename_spinner.setSelection(spinnerArrayAdapter.getCount());
                ingrename_spinner.setId(View.generateViewId());//아이디 자동  생성
                namespinner_id.add(ingrename_spinner.getId());//생성된 아이디 리스트에 삽입
                ingrename_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != ingrename_str.length - 1) {
                            ingreunit.setText(unitmap.get(ingrename_spinner.getSelectedItem().toString()));
                            ingreunit.setTextColor(getResources().getColor(R.color.blue));
                            ingreunit.setTextSize(18);
                            Log.d("spinner", "Spinner1: position=" + position + " id=" + id);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                ingreunit = new TextView(context);
                ingreunit.setText("단위");
                ingreunit.setTextColor(getResources().getColor(R.color.blue));
                ingreunit.setTextSize(18);
                //양 입력 텍스트 생성
                ingreamount_edit = new EditText(context);
                ingreamount_edit.setId(View.generateViewId());//아이디 자동  생성
                ingreamount_edit.setBackground(Drawable.createFromPath("@android:color/transparent"));
                ingreamount_edit.setHint("숫자 입력");
                amountedit_id.add(ingreamount_edit.getId());//생성된 아이디 리스트에 삽입

                linearlayout1.addView(ingrename);
                linearlayout1.addView(ingrename_spinner);
                linearlayout1.addView(ingreamount);
                linearlayout1.addView(ingreamount_edit);
                linearlayout1.addView(ingreunit);
                linearlayout.addView(linearlayout1);
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
            //Arraylist사이즈만큼 반복해서 재료와 양의 리스트 만들기
            for (int i = 0; i < namespinner_id.size(); i++) {
                Spinner spinnertemp;
                EditText editTexttemp;
                Recipe_Ingredients tempingredients = new Recipe_Ingredients();
                spinnertemp = findViewById(namespinner_id.get(i));
                editTexttemp = findViewById(amountedit_id.get(i));
                String temp_ingrename = spinnertemp.getSelectedItem().toString();
                float temp_amount = Float.parseFloat(editTexttemp.getText().toString());
                tempingredients.setName(temp_ingrename);
                tempingredients.setAmount(temp_amount);
                tempingredients.setUnit(unitmap.get(temp_ingrename));
                recipe_ingredients.add(tempingredients);
            }
            newRecipe.setRecipe_Ingredients(recipe_ingredients);

            Log.d("recipe",newRecipe.toString());
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
