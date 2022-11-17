package com.example.sogong.View;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Model.RecipePost;
import com.example.sogong.R;

public class RecipeLookupActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeId = getIntent().getIntExtra("recipeId",0);
        setContentView(R.layout.activity_lookuprecipe);
        Log.d("test", "Post_id = "+String.valueOf(recipeId));
    }
}
