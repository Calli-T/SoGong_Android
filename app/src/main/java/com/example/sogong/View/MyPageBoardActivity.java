package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlIngredients_f;
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMailList_f;
import com.example.sogong.Control.ControlMail_f;
import com.example.sogong.Control.ControlMyPhoto_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPhotoList_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlPost_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyPageBoardActivity extends AppCompatActivity {

    int type;
    String[] pagenum;
    public static int totalpage;
    public static List<RecipePost_f> recipelist;
    public static List<PhotoPost> photolist;

    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // ??????????????? ???????????? ???????????? ????????? ?????????
    private AtomicBoolean sortthreadFlag = new AtomicBoolean(); // ??????????????? ???????????? ???????????? ????????? ?????????

    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Custon_ProgressDialog custon_progressDialog;
    ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
    ControlMyPhoto_f cmpf = new ControlMyPhoto_f();

    Spinner sortspinner;
    ImageButton searchButton;

    Boolean firstpage;
    String currentSort;

    private GridView gridview = null;
    private PhotoAdapter photoAdapter = null;

    MyPageBoard_UI mpbu = new MyPageBoard_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("post_type", -1);
        Log.d("type", "type = " + type);

        if (type == 0) {//????????? ????????? ????????? ?????????
            Log.d("mypageRecipe", "??????");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//?????? ??????
            searchButton.setVisibility(View.VISIBLE);
            //????????? ????????? ????????? ??????????????????
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //????????? ??????
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //?????? ?????? ??????
            currentSort = "";
            // ??????) ????????? ????????? ?????? ?????? ??????
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;

                        if (recipelist.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("????????? ????????? ???????????? ????????????.");
                            noResult.setTextSize(30);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);

                        //????????? ????????? ????????? ??????????????? ?????? ????????? ???????????? visible??? ??????
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.VISIBLE);
                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " ??????");
                                String sort_str = sortspinner.getSelectedItem().toString();
                                if (!firstpage) {//??????????????? 1???????????? ????????????????????? 1???????????? ?????? ???????????? ?????? ?????? ????????? ???????????? ????????? ??????????????? ???????????? ?????????
                                    currentSort = sort_str;
                                    SortInfo sortInfo = new SortInfo(ControlLogin_f.userinfo.getNickname(), sort_str);
                                    cmrf.sortMyRecipeList(sortInfo);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                //??????????????? ????????? ???????????? ??????
                                                recipeAdapter.setRecipeList(recipelist);
                                                //????????? ?????????????????? ?????? ?????????
                                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClicked(int position, String data) {
                                                        Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent.putExtra("recipe_post", recipelist.get(position));
                                                        startActivity(intent);
                                                    }
                                                });
                                                custon_progressDialog.dismiss();
                                            } else if (responseCode == 404) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                custon_progressDialog.dismiss();
                                                mpbu.startDialog(0, "?????? ??????", "?????? ?????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                                            } else if (responseCode == 500) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                custon_progressDialog.dismiss();
                                                mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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

                                                if (sortthreadFlag.get()) {
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
                                    sortthreadFlag.set(true);
                                    //?????? ????????? ??????????????? ??? ???????????? ?????????
                                    t.start();
                                    //??????????????? ????????? ???????????? ??????
                                    recipeAdapter.setRecipeList(recipelist);
                                    //????????? ?????????????????? ?????? ?????????
                                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(int position, String data) {
                                            Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putExtra("recipe_post", recipelist.get(position));
                                            startActivity(intent);
                                        }
                                    });
                                    Log.d("recipefragment", "page spinner " + position + " ??????");
                                }
                                firstpage = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        //????????? ?????????????????? ?????? ?????????
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        //?????????????????? ??????
                        searchButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("isMyPage", true);
                                startActivity(intent);
                                //recipesearchActivity??? ????????????????????? ????????? ????????? ?????????
                            }
                        });
                        custon_progressDialog.dismiss();//????????? ??????
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 400) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "????????? ?????? ??????", "????????? ????????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
            cmrf.lookupMyRecipeList(ControlLogin_f.userinfo.getNickname());

            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //????????? ????????? ???????????? ???????????? ?????????
            t.start();
        } else if (type == 1) {
            //???????????????
            setContentView(R.layout.activity_myphotoboard);
            gridview = (GridView) findViewById(R.id.photo_grid);
            photoAdapter = new PhotoAdapter();
            gridview.setAdapter(photoAdapter);

            responseCode = 0;
            //????????? ??????
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            threadFlag.set(true);
            firstpage = true;

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (photolist.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("????????? ?????? ?????? ???????????? ????????????.");
                            noResult.setTextSize(30);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        photoAdapter.setPhotoList(photolist);
                        //???????????? ??????????????? ??????
                        gridview.setAdapter(photoAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("??????", "position = " + position + " id = " + id);
                                Intent intent = new Intent(MyPageBoardActivity.this, PhotoLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("photo_post", photolist.get(position));
                                startActivity(intent);
                            }
                        });
                        custon_progressDialog.dismiss();//????????? ??????
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 404) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ?????? ??????", "?????? ????????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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

            cmpf.lookupMyPhotoList(ControlLogin_f.userinfo.getNickname());

            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();


        } else if (type == 2) {
            //???????????? ?????????
            Log.d("mypageRecipe", "??????");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//?????? ??????
            searchButton.setVisibility(View.INVISIBLE);
            //????????? ????????? ????????? ??????????????????
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //????????? ??????
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //?????????????????? ??????
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            // ??????) ????????? ????????? ?????? ?????? ??????
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (recipelist.size() == 0) {//???????????? ???????????? ?????? ??????
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("???????????? ?????? ???????????? ????????????");
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }

                        //????????? ?????????????????? ?????? ?????????
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.INVISIBLE);
                        custon_progressDialog.dismiss();//????????? ??????
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ?????? ??????", "?????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                    } else if (responseCode == 502) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyLikeList(ControlLogin_f.userinfo.getNickname(), 1);
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //????????? ?????? ???????????? ???????????? ?????????
            t.start();
        } else if (type == 3) {
            //????????? ????????? ?????????
            Log.d("mypageRecipe", "??????");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//?????? ??????
            searchButton.setVisibility(View.INVISIBLE);
            //????????? ????????? ????????? ??????????????????
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //????????? ??????
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //?????????????????? ??????
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            // ??????) ????????? ????????? ?????? ?????? ??????
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (recipelist.size() == 0) {//?????? ????????? ???????????? ?????? ??????
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("????????? ????????? ???????????? ????????????.");
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //????????? ?????????????????? ?????? ?????????
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.INVISIBLE);
                        custon_progressDialog.dismiss();//????????? ??????
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "?????? ????????? ?????????", new ArrayList<>(Arrays.asList("??????")));
                    } else if (responseCode == 502) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyCommentList(ControlLogin_f.userinfo.getNickname());
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //????????? ????????? ???????????? ???????????? ?????????
            t.start();
        } else if (type == 4) {
            //????????? ?????? ?????? ????????? ??????
            setContentView(R.layout.activity_myphotoboard);
            gridview = (GridView) findViewById(R.id.photo_grid);
            photoAdapter = new PhotoAdapter();
            gridview.setAdapter(photoAdapter);

            responseCode = 0;
            //????????? ??????
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            threadFlag.set(true);
            firstpage = true;

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (photolist.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("???????????? ?????? ???????????? ????????????");
                            noResult.setVisibility(View.VISIBLE);
                        }
                        photoAdapter.setPhotoList(photolist);
                        //???????????? ??????????????? ??????
                        gridview.setAdapter(photoAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("??????", "position = " + position + " id = " + id);
                                Intent intent = new Intent(MyPageBoardActivity.this, PhotoLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("photo_post", photolist.get(position));
                                startActivity(intent);
                            }
                        });
                        custon_progressDialog.dismiss();//????????? ??????
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 404) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ?????? ??????", "?????? ????????? ??????????????????.", new ArrayList<>(Arrays.asList("??????")));
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//????????? ??????
                        mpbu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyLikeList(ControlLogin_f.userinfo.getNickname(), 2);
            threadFlag.set(true);
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();

        }

    }


    class MyPageBoard_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO ???????????? ???????????? ???????????? (????????? ??????)
            //toast.setGravity(Gravity.TOP, 0, 0); //TODO ???????????? ???????????? ???????????? (?????? ??????)
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO ???????????? ???????????? ???????????? (?????? ??????)
            toast.setDuration(Toast.LENGTH_SHORT); //????????? ?????? ??????
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(MyPageBoardActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
        }
    }
}
