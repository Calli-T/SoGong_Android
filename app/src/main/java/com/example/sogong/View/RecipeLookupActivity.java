package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlIngredients_f;
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.RecipePostLookUp;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.R;
import com.google.android.libraries.places.internal.zzahn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeLookupActivity extends AppCompatActivity {
    TextView recipetitle;
    TextView recipecategory;
    ImageView[] pepper;
    TextView recipedescription;
    TextView recipecomment;
    public CommentAdapter commentAdapter;
    public RecyclerView commentRecyclerView;
    ImageButton menubutton;
    ImageButton like_btn;
    Button comment_add_btn;
    Button remain_amount_btn;
    EditText comment_edit;
    ImageButton back_button;

    public Recipe_Ingre_Adapter recipeIngreAdapter;
    public RecyclerView recipeIngreRecyclerView;
    Custon_ProgressDialog custon_progressDialog;


    // 응답 코드
    public static AtomicInteger responseCode = new AtomicInteger();
    public static AtomicInteger responseCode2 = new AtomicInteger();
    public static AtomicInteger responseCode3 = new AtomicInteger();

    // 반환값 저장
    public static RecipePostLookUp recipePostLookUp;
    public static List<Recipe_Ingredients> unExistIngredients;
    public static int commentResult;

    // 스레드 제어용 플래그
    private AtomicBoolean threadFlag = new AtomicBoolean();
    private AtomicBoolean commentthreadFlag = new AtomicBoolean();
    private AtomicBoolean commenteditthreadFlag = new AtomicBoolean();
    private AtomicBoolean commentdeletethreadFlag = new AtomicBoolean();

    RecipePost_f recipePostF;
    PopupMenu dropDownMenu;
    Menu menu;
    RecipeLookup_UI rlu = new RecipeLookup_UI();

    Boolean likedState;
    Boolean isProgress;

    private ArrayList<Integer> isExist = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipePostF = getIntent().getParcelableExtra("recipe_post");

        responseCode.set(0);
        responseCode2.set(0);

        RecipeLookup_UI rlu = new RecipeLookup_UI();

        //사용할 컴포넌트들 등록
        Log.d("recipe", recipePostF.toString());
        setContentView(R.layout.activity_lookuprecipe);
        recipetitle = findViewById(R.id.recipetitle_text1);
        recipecategory = findViewById(R.id.recipecate_text1);
        pepper = new ImageView[5];
        pepper[0] = findViewById(R.id.pepper_1);
        pepper[1] = findViewById(R.id.pepper_2);
        pepper[2] = findViewById(R.id.pepper_3);
        pepper[3] = findViewById(R.id.pepper_4);
        pepper[4] = findViewById(R.id.pepper_5);
        recipedescription = findViewById(R.id.recipedescription_text);
        recipecomment = findViewById(R.id.commentcount_text);
        like_btn = findViewById(R.id.like_btn);
        comment_edit = findViewById(R.id.comment_edit);
        comment_add_btn = findViewById(R.id.comment_add_btn);
        remain_amount_btn = findViewById(R.id.ingrecal_btn);

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        //뒤로가기버튼
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //사용자에 따른 옵션 메뉴 로직 시작
        menubutton = findViewById(R.id.menu_button);
        dropDownMenu = new PopupMenu(this, menubutton);
        menu = dropDownMenu.getMenu();
        if (ControlLogin_f.userinfo.getNickname().equals(recipePostF.getNickname())) {
            menu.add(0, 0, 0, "삭제하기");
            menu.add(0, 1, 0, "수정하기");
        } else {
            menu.add(0, 2, 0, "쪽지 보내기");
            menu.add(0, 3, 0, "신고하기");
        }
        //옵션 메뉴 클릭 리스너
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        //게시글 삭제 로직 추가
                        Log.d("recipe", "삭제하기 menu click");


                        rlu.startDialog(1, "게시글 삭제", "게시글을 삭제하시겠습니까?", new ArrayList<>(Arrays.asList("삭제", "취소")));
                        class NewRunnable implements Runnable {
                            NewRunnable() {
                            }

                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(100);
                                        if (Custom_Dialog.state == 0) {
                                            Custom_Dialog.state = -1;
                                            isProgress = true;
                                            final Runnable progress = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (isProgress) {
                                                        custon_progressDialog.show();
                                                    } else custon_progressDialog.dismiss();
                                                }
                                            };
                                            runOnUiThread(progress);
                                            threadFlag.set(true);
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (responseCode.get() == 200) {
                                                        responseCode.set(-1);
                                                        isProgress = false;
                                                        runOnUiThread(progress);
                                                        threadFlag.set(false);
                                                        Log.d("레시피 삭제", "성공");
                                                        onBackPressed();
                                                    } else if (responseCode.get() == 500) {
                                                        responseCode.set(-1);
                                                        threadFlag.set(false);
                                                        Log.d("레시피 삭제", "성공");
                                                        isProgress = false;
                                                        runOnUiThread(progress);
                                                        rlu.startDialog(0, "게시글 삭제", "삭제에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                                    } else if (responseCode.get() == 502) {
                                                        responseCode.set(-1);
                                                        threadFlag.set(false);
                                                        Log.d("레시피 삭제", "성공");
                                                        isProgress = false;
                                                        runOnUiThread(progress);
                                                        rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                                    }
                                                }
                                            };

                                            class NewRunnable1 implements Runnable {
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
                                            ControlRecipe_f crf = new ControlRecipe_f();
                                            crf.deleteRecipe(ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id());
                                            threadFlag.set(true);
                                            NewRunnable1 nr = new NewRunnable1();
                                            Thread t = new Thread(nr);
                                            t.start();
                                            break;
                                        } else if (Custom_Dialog.state == 1) {
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        NewRunnable nr = new NewRunnable();
                        Thread t = new Thread(nr);
                        //삭제할건지 되묻는 다이얼로그에서 선택을 기다리는 쓰레드
                        t.start();
                        return true;
                    case 1:
                        //게시글 수정 로직 추가
                        Intent intent2 = new Intent(RecipeLookupActivity.this, RecipeAddActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent2.putExtra("isEdit", true);
                        intent2.putExtra("EditRecipe", recipePostLookUp.getRecipeInfo());
                        startActivity(intent2);

                        Log.d("recipe", "수정하기 menu click");

                        return true;
                    case 2:
                        //쪽지보내기 로직 추가
                        Intent intent1 = new Intent(RecipeLookupActivity.this, MailSendActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.putExtra("mail_receiver", recipePostF.getNickname());
                        startActivity(intent1);
                        Log.d("recipe", "쪽지 보내기 menu click");
                        return true;
                    case 3:
                        //신고하기 로직 추가
                        Intent intent = new Intent(RecipeLookupActivity.this, ReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("recipe_id", recipePostF.getPost_id());
                        intent.putExtra("report_post_type", 1);
                        startActivity(intent);
                        Log.d("recipe", "신고하기 menu click");
                        return true;
                }
                return false;
            }
        });
        //메뉴버튼 클릭 리스너
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });

        //댓글 리사이클러뷰 구현
        commentRecyclerView = (RecyclerView) findViewById(R.id.recipe_comment_recyclerview);
        commentAdapter = new CommentAdapter();
        commentRecyclerView.setAdapter(commentAdapter);
        RecyclerView.LayoutManager commentlayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(commentlayoutManager);

        //재료 리사이클러뷰 구현
        recipeIngreRecyclerView = (RecyclerView) findViewById(R.id.recipe_ingre_recyclerview);
        recipeIngreAdapter = new Recipe_Ingre_Adapter();
        recipeIngreRecyclerView.setAdapter(recipeIngreAdapter);
        RecyclerView.LayoutManager recipe_ingre_layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recipeIngreRecyclerView.setLayoutManager(recipe_ingre_layoutManager);

        //넘겨온 값으로 채워넣음
        recipetitle.setText(recipePostF.getTitle());
        recipecategory.setText(recipePostF.getCategory());
        recipedescription.setText(recipePostF.getDescription());
        switch (recipePostF.getDegree_of_spicy()) {
            case 0:
                pepper[0].setVisibility(View.INVISIBLE);
                pepper[1].setVisibility(View.INVISIBLE);
                pepper[2].setVisibility(View.INVISIBLE);
                pepper[3].setVisibility(View.INVISIBLE);
                pepper[4].setVisibility(View.INVISIBLE);
                break;
            case 1:
                pepper[0].setVisibility(View.VISIBLE);
                pepper[1].setVisibility(View.INVISIBLE);
                pepper[2].setVisibility(View.INVISIBLE);
                pepper[3].setVisibility(View.INVISIBLE);
                pepper[4].setVisibility(View.INVISIBLE);
                break;
            case 2:
                pepper[0].setVisibility(View.VISIBLE);
                pepper[1].setVisibility(View.VISIBLE);
                pepper[2].setVisibility(View.INVISIBLE);
                pepper[3].setVisibility(View.INVISIBLE);
                pepper[4].setVisibility(View.INVISIBLE);
                break;
            case 3:
                pepper[0].setVisibility(View.VISIBLE);
                pepper[1].setVisibility(View.VISIBLE);
                pepper[2].setVisibility(View.VISIBLE);
                pepper[3].setVisibility(View.INVISIBLE);
                pepper[4].setVisibility(View.INVISIBLE);
                break;
            case 4:
                pepper[0].setVisibility(View.VISIBLE);
                pepper[1].setVisibility(View.VISIBLE);
                pepper[2].setVisibility(View.VISIBLE);
                pepper[3].setVisibility(View.VISIBLE);
                pepper[4].setVisibility(View.INVISIBLE);
                break;
            case 5:
                pepper[0].setVisibility(View.VISIBLE);
                pepper[1].setVisibility(View.VISIBLE);
                pepper[2].setVisibility(View.VISIBLE);
                pepper[3].setVisibility(View.VISIBLE);
                pepper[4].setVisibility(View.VISIBLE);
                break;
        }
        recipecomment.setText("댓글 " + String.valueOf(recipePostF.getComment_count() + "개"));
        commentAdapter.setCommentList(recipePostF.getComments());
        recipeIngreAdapter.setRecipeIngreList(recipePostF.getRecipe_Ingredients());
    }

    @Override
    public void onResume() {
        super.onResume();
custon_progressDialog.show();
        // 값 변동을 고려하여 열람 시점에서 새로 API 호출
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode.get() == 200 && responseCode2.get() == 200) {
                    responseCode.set(-1);
                    responseCode2.set(-1);
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();
                    //댓글 리사이클러뷰 구현
                    commentRecyclerView = (RecyclerView) findViewById(R.id.recipe_comment_recyclerview);
                    commentAdapter = new CommentAdapter();
                    commentRecyclerView.setAdapter(commentAdapter);
                    RecyclerView.LayoutManager commentlayoutManager = new LinearLayoutManager(RecipeLookupActivity.this);
                    commentRecyclerView.setLayoutManager(commentlayoutManager);

                    //재료 리사이클러뷰 구현
                    recipeIngreRecyclerView = (RecyclerView) findViewById(R.id.recipe_ingre_recyclerview);
                    recipeIngreAdapter = new Recipe_Ingre_Adapter();
                    recipeIngreRecyclerView.setAdapter(recipeIngreAdapter);
                    RecyclerView.LayoutManager recipe_ingre_layoutManager = new LinearLayoutManager(RecipeLookupActivity.this, RecyclerView.VERTICAL, false);
                    recipeIngreRecyclerView.setLayoutManager(recipe_ingre_layoutManager);

                    //넘겨온 값으로 채워넣음
                    recipetitle.setText(recipePostLookUp.getRecipeInfo().getTitle());
                    recipecategory.setText(recipePostLookUp.getRecipeInfo().getCategory());
                    switch (recipePostLookUp.getRecipeInfo().getDegree_of_spicy()) {
                        case 0:
                            pepper[0].setVisibility(View.INVISIBLE);
                            pepper[1].setVisibility(View.INVISIBLE);
                            pepper[2].setVisibility(View.INVISIBLE);
                            pepper[3].setVisibility(View.INVISIBLE);
                            pepper[4].setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            pepper[0].setVisibility(View.VISIBLE);
                            pepper[1].setVisibility(View.INVISIBLE);
                            pepper[2].setVisibility(View.INVISIBLE);
                            pepper[3].setVisibility(View.INVISIBLE);
                            pepper[4].setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            pepper[0].setVisibility(View.VISIBLE);
                            pepper[1].setVisibility(View.VISIBLE);
                            pepper[2].setVisibility(View.INVISIBLE);
                            pepper[3].setVisibility(View.INVISIBLE);
                            pepper[4].setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            pepper[0].setVisibility(View.VISIBLE);
                            pepper[1].setVisibility(View.VISIBLE);
                            pepper[2].setVisibility(View.VISIBLE);
                            pepper[3].setVisibility(View.INVISIBLE);
                            pepper[4].setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            pepper[0].setVisibility(View.VISIBLE);
                            pepper[1].setVisibility(View.VISIBLE);
                            pepper[2].setVisibility(View.VISIBLE);
                            pepper[3].setVisibility(View.VISIBLE);
                            pepper[4].setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            pepper[0].setVisibility(View.VISIBLE);
                            pepper[1].setVisibility(View.VISIBLE);
                            pepper[2].setVisibility(View.VISIBLE);
                            pepper[3].setVisibility(View.VISIBLE);
                            pepper[4].setVisibility(View.VISIBLE);
                            break;
                    }
                    recipedescription.setText(recipePostLookUp.getRecipeInfo().getDescription());
                    recipecomment.setText("댓글 " + String.valueOf(recipePostLookUp.getRecipeInfo().getComment_count() + "개"));
                    commentAdapter.setCommentList(recipePostLookUp.getRecipeInfo().getComments());

                    for (int i = 0; i < unExistIngredients.size(); i++)
                        isExist.add(unExistIngredients.get(i).getId());
                    recipeIngreAdapter.setRecipeIngreListExist(recipePostLookUp.getRecipeInfo().getRecipe_Ingredients(), isExist);

                    //댓글리스트에 있는 버튼들의 클릭 이벤트 처리
                    commentAdapter.setOnItemLeftButtonClickListener(new CommentAdapter.OnItemLeftButtonClickListener() {
                        @Override
                        public void onItemLeftButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "left button click");
                            comment_edit.setText(recipePostLookUp.getRecipeInfo().getComments().get(position).getComments());
                            comment_add_btn.setText("수정");
                            comment_add_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /* #34 레시피 게시판 댓글 수정 */
                                    if (comment_edit.getText().toString().equals("")) {
                                        rlu.startDialog(0, "댓글 수정", "댓글 작성란이 비어있습니다.", new ArrayList<>(Arrays.asList("확인")));
                                    } else {
                                        commenteditthreadFlag.set(true);
                                        custon_progressDialog.show();
                                        final Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (responseCode.get() == 200) {
                                                    Log.d("댓글 수정", "성공");
                                                    commenteditthreadFlag.set(false);
                                                    responseCode.set(-1);
                                                    Intent intent = getIntent();
                                                    finish(); //현재 액티비티 종료 실시
                                                    overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                                                    startActivity(intent); //현재 액티비티 재실행 실시
                                                    overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                                                    custon_progressDialog.dismiss();
                                                } else if (responseCode.get() == 500) {
                                                    commenteditthreadFlag.set(false);
                                                    responseCode.set(-1);
                                                    rlu.startToast("댓글 수정 실패");
                                                    custon_progressDialog.dismiss();
                                                } else if (responseCode.get() == 502) {
                                                    commenteditthreadFlag.set(false);
                                                    responseCode.set(-1);
                                                    custon_progressDialog.dismiss();
                                                    rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                                                    if (commenteditthreadFlag.get())
                                                        runOnUiThread(runnable);
                                                    else {
                                                        i = 30;
                                                    }
                                                }
                                            }
                                        }
                                        Comment newComment = new Comment(recipePostLookUp.getRecipeInfo().getComments().get(position).getComment_id(), ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id(), comment_edit.getText().toString(), "");
                                        comment_add_btn.setText("등록");
                                        comment_edit.setText("");
                                        ControlComment_f ccf = new ControlComment_f();
                                        ccf.editComment(newComment);

                                        NewRunnable nr = new NewRunnable();
                                        Thread t = new Thread(nr);
                                        t.start();
                                    }
                                }
                            });
                        }
                    });
                    commentAdapter.setOnItemRightButtonClickListener(new CommentAdapter.OnItemRightButtonClickListener() {
                        @Override
                        public void onItemRightButtonClick(View view, int position) {
                            Log.d("recipe", String.valueOf(position) + "번 댓글 삭제");
                            //로그인한 사용자와 댓글을 단 사용자와 같은 경우는 삭제 로직
                            commentdeletethreadFlag.set(true);
                            if (Objects.equals(ControlLogin_f.userinfo.getNickname(), recipePostLookUp.getRecipeInfo().getComments().get(position).getNickname())) {


                                rlu.startDialog(1, "댓글 삭제", "댓글을 삭제하시겠습니까?", new ArrayList<>(Arrays.asList("삭제", "취소")));

                                class NewRunnable implements Runnable {
                                    NewRunnable() {
                                    }
                                    @Override
                                    public void run() {
                                        while (true) {
                                            try {
                                                Thread.sleep(100);
                                                if (Custom_Dialog.state == 0) {
                                                    Custom_Dialog.state = -1;
                                                    isProgress = true;
                                                    final Runnable progress = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (isProgress) {
                                                                custon_progressDialog.show();
                                                            } else custon_progressDialog.dismiss();
                                                            ;
                                                        }
                                                    };
                                                    runOnUiThread(progress);
                                                    Log.d("recipe", String.valueOf(position) + "번 댓글 삭제");
                                                    final Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (responseCode.get() == 200) {
                                                                Log.d("댓글 삭제", "성공");
                                                                responseCode.set(-1);
                                                                commentdeletethreadFlag.set(false);
                                                                isProgress = false;
                                                                runOnUiThread(progress);
                                                                Intent intent = getIntent();
                                                                finish(); //현재 액티비티 종료 실시
                                                                overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                                                                startActivity(intent); //현재 액티비티 재실행 실시
                                                                overridePendingTransition(0, 0); //인텐트 애니메이션 없애기

                                                            } else if (responseCode.get() == 500) {
                                                                responseCode.set(-1);
                                                                commentdeletethreadFlag.set(false);
                                                                isProgress = false;
                                                                runOnUiThread(progress);
                                                                rlu.startDialog(0,"댓글 삭제","댓글 삭제를 실패했습니다.",new ArrayList<>(Arrays.asList("확인")));
                                                            } else if (responseCode.get() == 502) {
                                                                responseCode.set(-1);
                                                                commentdeletethreadFlag.set(false);
                                                                isProgress = false;
                                                                runOnUiThread(progress);
                                                                rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                                            }
                                                        }
                                                    };
                                                    class NewRunnable1 implements Runnable {
                                                        @Override
                                                        public void run() {
                                                            for (int i = 0; i < 30; i++) {
                                                                try {
                                                                    Thread.sleep(1000);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.d("댓글 삭제", "responsecode = " + responseCode.get());

                                                                if (commentdeletethreadFlag.get())
                                                                    runOnUiThread(runnable);
                                                                else {
                                                                    i = 30;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    ControlComment_f ccf = new ControlComment_f();
                                                    ccf.deleteComment(ControlLogin_f.userinfo.getNickname(), recipePostLookUp.getRecipeInfo().getComments().get(position).getComment_id());

                                                    NewRunnable1 nr = new NewRunnable1();
                                                    Thread t = new Thread(nr);
                                                    t.start();


                                                    break;
                                                } else if (Custom_Dialog.state == 1) {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                NewRunnable nr = new NewRunnable();
                                Thread t = new Thread(nr);
                                t.start();


                            } else {//같지 않은 경우는 신고 로직
                                Log.d("recipe", String.valueOf(position) + "번 댓글 신고");
                                Intent intent = new Intent(RecipeLookupActivity.this, ReportActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("comment_id", recipePostLookUp.getRecipeInfo().getComments().get(position).getComment_id());
                                intent.putExtra("report_post_type", -1);
                                startActivity(intent);

                            }
                            /* #35 레시피 게시판 댓글 삭제 */
                            //ccf.deleteComment("test", 8);
                            /* #36 레시피 게시판 댓글 신고 */
                            //Report reportInfo = new Report("test", "android comment report test", 28, -1);
                            //cref.reportComment(reportInfo);
                        }
                    });
                    likedState = recipePostLookUp.isLikeInfo();
                    if (likedState) {
                        like_btn.setBackground(getDrawable(R.drawable.thumb_up_fill));
                    } else like_btn.setBackground(getDrawable(R.drawable.thumb_up));
                    like_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (likedState) {
                                like_btn.setBackground(getDrawable(R.drawable.thumb_up));
                                likedState = false;
                            } else {
                                like_btn.setBackground(getDrawable(R.drawable.thumb_up_fill));
                                likedState = true;
                            }
                        }
                    });

                    comment_add_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //댓글 등록 관련 로직 추가
                            //새로고침필수
                            /* #33 레시피 게시판 댓글 작성 */
                            if (comment_edit.getText().toString().equals("")) {
                                rlu.startDialog(0, "댓글 작성", "댓글 작성란이 비어있습니다.", new ArrayList<>(Arrays.asList("확인")));
                            } else {
                                custon_progressDialog.show();
                                commentthreadFlag.set(true);
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseCode.get() == 200) {
                                            responseCode.set(-1);
                                            commentthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            Log.d("댓글 작성", "성공");
                                            Intent intent = getIntent();
                                            finish(); //현재 액티비티 종료 실시
                                            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                                            startActivity(intent); //현재 액티비티 재실행 실시
                                            overridePendingTransition(0, 0); //인텐트 애니메이션 없애기

                                        } else if (responseCode.get() == 403) {
                                            responseCode.set(-1);
                                            commentthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            rlu.startDialog(0, "댓글 등록", "댓글 등록에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                        } else if (responseCode.get() == 500) {
                                            responseCode.set(-1);
                                            commentthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                                            if (commentthreadFlag.get())
                                                runOnUiThread(runnable);
                                            else {
                                                i = 30;
                                            }
                                        }
                                    }
                                }


                                ControlComment_f ccf = new ControlComment_f();
                                Comment newComment = new Comment(0, ControlLogin_f.userinfo.getNickname(), recipePostLookUp.getRecipeInfo().getPost_id(), comment_edit.getText().toString(), "");
                                ccf.writeComment(newComment);
                                NewRunnable nr = new NewRunnable();
                                Thread t = new Thread(nr);
                                responseCode.set(-1);
                                t.start();
                                comment_edit.setText("");
                                Log.d("댓글 내용", newComment.toString());
                            }
                        }
                    });
                    remain_amount_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            custon_progressDialog.show();

                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (responseCode3.get() == 200) {
                                        //rlu.startToast("남는재료 계산 완료");
                                        threadFlag.set(false);
                                    } else {
                                        rlu.startDialog(0, "저장 실패", "감산결과를 저장하는데 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                        threadFlag.set(false);
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
                                            custon_progressDialog.dismiss();
                                        }
                                    }
                                }
                            }

                            if (responseCode.get() == -1 && responseCode2.get() == -1) {
                                ControlIngredients_f cif = new ControlIngredients_f();
                                responseCode.set(-2);
                                responseCode2.set(-2);
                                cif.remainAmmounts(ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id());
                            }
                            NewRunnable nr = new NewRunnable();
                            Thread t = new Thread(nr);
                            t.start();
                        }
                    });

                    custon_progressDialog.dismiss();

                } else if (responseCode.get() == 500 && responseCode2.get() == 500) {
                    responseCode.set(-1);
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();
                    if (responseCode2.get() == 500) {
                        rlu.startDialog(0, "재료 요청 실패", "없는 재료를 가져오는데 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode.get() == 500) {
                        rlu.startToast("게시글을 가져오는데 실패했습니다.");
                    }
                } else if (responseCode.get() == 502 && responseCode2.get() == 502) {
                    responseCode.set(-1);
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();
                    rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                    if(threadFlag.get()){
                    runOnUiThread(runnable);
                    }else {
                        i=30;
                    }
                }
            }
        }

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(recipePostF.getPost_id(), ControlLogin_f.userinfo.getNickname());
        ControlIngredients_f cif = new ControlIngredients_f();
        cif.lookupUnExistIngredients(ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id());
threadFlag.set(true);
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        responseCode.set(-1);
        responseCode2.set(-1);
        t.start();
    }

    // 뒤로가기 버튼 입력과 동시에 서버에 좋아요 등록/취소 반영
    @Override
    public void onBackPressed() {
        if (likedState != recipePostLookUp.isLikeInfo()) {
            custon_progressDialog.show();
            if (recipePostLookUp.isLikeInfo()) {//좋아요 취소를 하려는 경우
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode.get() == 200) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            RecipeLookupActivity.super.onBackPressed();
                        } else if (responseCode.get() == 500) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            rlu.startDialog(0, "서버 오류", "\"좋아요\" 취소를 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                        } else if (responseCode.get() == 502) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                            Log.d("사진액티비티", "responsecode = " + responseCode);
                            if (threadFlag.get())
                                runOnUiThread(runnable);
                            else {
                                i = 30;
                            }
                        }
                    }
                }
                ControlLike_f clf = new ControlLike_f();
                Log.d("like", "좋아요 취소한 거 서버로 보내요");
                clf.unLikePost(ControlLogin_f.userinfo.getNickname(), 1, recipePostLookUp.getRecipeInfo().getPost_id());

                threadFlag.set(true);
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            } else {
                final Runnable runnable = new Runnable() {//좋아요 등록을 하려는 경우
                    @Override
                    public void run() {
                        if (responseCode.get() == 200) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            RecipeLookupActivity.super.onBackPressed();
                        } else if (responseCode.get() == 501) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            rlu.startDialog(0, "서버 오류", "\"좋아요\" 등록을 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                        } else if (responseCode.get() == 502) {
                            responseCode.set(-1);
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                ControlLike_f clf = new ControlLike_f();
                clf.likePost(ControlLogin_f.userinfo.getNickname(), 1, recipePostLookUp.getRecipeInfo().getPost_id());
                Log.d("like", "좋아요 누른 거 서버로 보내요");
                threadFlag.set(true);
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        } else RecipeLookupActivity.super.onBackPressed();
    }

    class RecipeLookup_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(RecipeLookupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RecipeLookupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
