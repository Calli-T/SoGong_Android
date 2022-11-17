package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.R;

public class AddIngredientActivity extends AppCompatActivity {
    public static int responseCode;
    Button ingreadd_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingredient);

        /*
        ingreadd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddIngredientActivity.this)
            }
        });
        */
    }
}
