package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeSearchActivity extends AppCompatActivity {
    public static int responseCode;

    Spinner searchOption;
    EditText searchEdit;
    ImageButton searchButton;
    Button cateSearchButton;
    ChipGroup chipGroup;
    ChipGroup chipGroup2;
    StringBuilder categories;

    String[] category_str = new String[]{"족발 보쌈", "찜 탕 찌개", "돈까스", "회", "일식", "피자", "고기구이", "야식", "양식", "치킨", "중식", "아시안", "백반", "죽", "국수", "도시락", "분식", "카페 디저트", "패스트푸드"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchrecipe);
        categories = new StringBuilder();

        searchOption = findViewById(R.id.searchoption_spinner);
        searchEdit = findViewById(R.id.search_edit);
        searchButton = findViewById(R.id.search_button);
        cateSearchButton = findViewById(R.id.cate_search_button);
        chipGroup = findViewById(R.id.chipgroup);
        chipGroup2 = findViewById(R.id.chipgroup2);
        for (int i = 0; i < category_str.length; i++) {//카테고리 버튼들 추가
            Chip chip = new Chip(RecipeSearchActivity.this);
            chip.setText(category_str[i]);
            chipGroup.addView(chip);
            //카테고리 버튼들 클릭 시 chipGroup2영역에 추가
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chip.setEnabled(false);
                    Chip chip2 = new Chip(RecipeSearchActivity.this);
                    chip2.setText(chip.getText());
                    chip2.setCloseIconVisible(true);
                    chipGroup2.addView(chip2);
                    chip2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup2.removeView(v);
                            chip.setEnabled(true);
                        }
                    });
                    chip2.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup2.removeView(v);
                            chip.setEnabled(true);
                        }
                    });
                }
            });
        }
        //타이핑 검색 버튼 클릭 시
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeSearchActivity.this, RecipeSearchResultActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("searchType", "타이핑");
                intent.putExtra("categories", "");
                intent.putExtra("keywordType", searchOption.getSelectedItem().toString());
                intent.putExtra("keyword", searchEdit.getText().toString());
                startActivity(intent);
            }
        });
        //카테고리 검색 버튼 클릭 시
        cateSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chipGroup2.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup2.getChildAt(i);
                    categories.append(chip.getText().toString()).append("-");
                }
                Intent intent = new Intent(RecipeSearchActivity.this, RecipeSearchResultActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("searchType", "카테고리");
                intent.putExtra("categories", categories.deleteCharAt(categories.length() - 1).toString());
                intent.putExtra("keywordType","");
                intent.putExtra("keyword", "");
                startActivity(intent);
            }
        });

    }
}
