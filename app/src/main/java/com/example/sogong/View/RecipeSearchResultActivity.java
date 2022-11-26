package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeSearchResultActivity extends AppCompatActivity {
    public static int responseCode;
    public static List<RecipePost_f> recipeList;
    public static int totalpage;
    String[] pagenum;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    TextView searchContent;
    Spinner pageSpinner;
    Button researchButton;
    Custon_ProgressDialog custon_progressDialog;

    ControlRecipeList_f crlf = new ControlRecipeList_f();

    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Boolean firstpage;
    String searchType;
    String categories;
    String keywordType;
    String keyword;

    int currentPage;

    StringBuilder searchContent_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        searchType = getIntent().getStringExtra("searchType");
        categories = getIntent().getStringExtra("categories");
        keywordType = getIntent().getStringExtra("keywordType");
        keyword = getIntent().getStringExtra("keyword");

        searchContent = findViewById(R.id.search_text);
        researchButton = findViewById(R.id.re_search_button);

        //레시피 게시글 리스트 리사이클러뷰
        recipeRecyclerView = (RecyclerView) findViewById(R.id.search_recipe_recyclerview);
        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeSearchResultActivity.this);
        recipeRecyclerView.setLayoutManager(layoutManager);

        searchContent_str = new StringBuilder();
        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(RecipeSearchResultActivity.this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();
        firstpage = true;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    threadFlag.set(false);
                    responseCode = -1;
                    if (searchType.equals("키워드")) {
                        searchContent_str.append(keywordType);
                        searchContent_str.append(" : ");
                        searchContent_str.append(keyword);
                        searchContent.setText(searchContent_str);
                    } else if (searchType.equals("카테고리")) {
                        searchContent_str.append(categories);
                        searchContent.setText(searchContent_str);
                    }
                    recipeAdapter.setRecipeList(recipeList);
                    pagenum = new String[totalpage];
                    for (int i = 1; i <= totalpage; i++) {
                        pagenum[i - 1] = String.valueOf(i);
                    }
                    //페이지 수 스피너 설정
                    pageSpinner = findViewById(R.id.recipe_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RecipeSearchResultActivity.this, android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pageSpinner.setAdapter(adapter1);
                    pageSpinner.setPrompt("이동할 페이지");
                    pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentPage = position + 1;
                                crlf.searchRecipeList(searchType, categories, keywordType, keyword, position + 1);
                                custon_progressDialog.show();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseCode == 200) {
                                            responseCode = -1;
                                            pagethreadFlag.set(false);
                                            //업데이트된 레시피 리스트로 전환
                                            recipeAdapter.setRecipeList(recipeList);
                                            //레시피 리사이클러뷰 클릭 이벤트
                                            recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClicked(int position, String data) {
                                                    Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("recipe_post", recipeList.get(position));
                                                    startActivity(intent);
                                                    //+조회수 관련 로직 추가할 것
                                                }
                                            });
                                            custon_progressDialog.dismiss();
                                        }

                                    }
                                };
                                class NewRunnable implements Runnable {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 30; i++) {
                                            try {
                                                sleep(1000);
                                                Log.d("thread2", "thread2");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (pagethreadFlag.get()) {
                                                runOnUiThread(runnable);
                                                Log.d("thread2", "thread3");
                                            } else {
                                                i = 30;
                                                Log.d("thread2", "thread4");
                                            }
                                        }
                                        Log.d("thread2", "thread1");
                                    }
                                }
                                NewRunnable nr = new NewRunnable();
                                Thread t = new Thread(nr);
                                pagethreadFlag.set(true);
                                t.start();
                                //업데이트된 레시피 리스트로 전환
                                recipeAdapter.setRecipeList(recipeList);
                                //레시피 리사이클러뷰 클릭 이벤트
                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("recipe_post", recipeList.get(position));
                                        startActivity(intent);
                                    }
                                });
                                Log.d("recipefragment", "page spinner " + position + " 클릭");
                            }
                            firstpage = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //레시피 리사이클러뷰 클릭 이벤트
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("recipe_post", recipeList.get(position));
                            startActivity(intent);
                            //+조회수 관련 로직 추가할 것
                        }
                    });
                    custon_progressDialog.dismiss();//로딩창 종료
                    Thread.currentThread().interrupt();
                } else if (responseCode == 500) {
                    threadFlag.set(false);
                    responseCode = -1;
                    custon_progressDialog.dismiss();//로딩창 종료
                    Thread.currentThread().interrupt();
                } else if (responseCode == 502) {
                    threadFlag.set(false);
                    responseCode = -1;
                    custon_progressDialog.dismiss();//로딩창 종료
                    Thread.currentThread().interrupt();
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

                    if (threadFlag.get())
                        runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }
        crlf.searchRecipeList(searchType, categories, keywordType, keyword, 1);
        threadFlag.set(true);
        responseCode = 0;
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        researchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
