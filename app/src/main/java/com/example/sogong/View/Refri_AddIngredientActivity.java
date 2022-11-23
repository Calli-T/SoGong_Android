package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Refri_AddIngredientActivity extends AppCompatActivity {
    public static int responseCode;
    Spinner ingreName;
    EditText ingreAmount;
    Spinner ingreUnit;
    EditText ingreExpiredate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingredient);
        ingreName = findViewById(R.id.ingretitle_spinner);
        ingreAmount = findViewById(R.id.ingreamount_edit);
        ingreUnit = findViewById(R.id.ingre_unit_spinner);
        ingreExpiredate = findViewById(R.id.ingre_expiredate_edit);

        FloatingActionButton fab =findViewById(R.id.recipe_add_button);
        fab.setOnClickListener(new FABClickListener());
        /*
        ingreadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddIngredientActivity.this)
            }
        });
        */

    }
    class FABClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Refrigerator ingredient = new Refrigerator(0,ingreName.getSelectedItem().toString(), ControlLogin_f.userinfo.getNickname(), ingreUnit.getSelectedItem().toString(),Float.parseFloat(ingreAmount.getText().toString()),ingreExpiredate.getText().toString());
            /* #7 사용자 보유 재료 추가 */
            //Refrigerator ingredients = new Refrigerator(0, "yangpa", "test", "Kg", 1, "2022-11-11");
            //crf.addRefrigerator(ingredients);

        }
    }
}
