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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.List;
import java.util.Objects;

public class RecipeLookupActivity extends AppCompatActivity {
    TextView recipetitle;
    TextView recipecategory;
    TextView recipespicy;
    TextView recipedescription;
    TextView recipecomment;
    public CommentAdapter commentAdapter;
    public RecyclerView commentRecyclerView;
    ImageButton menubutton;
    ImageButton like_btn;
    Button comment_add_btn;
    EditText comment_edit;

    public Recipe_Ingre_Adapter recipeIngreAdapter;
    public RecyclerView recipeIngreRecyclerView;

    public static int responseCode;
    public static RecipePost_f recipePostF;

    PopupMenu dropDownMenu;
    Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecipePost_f recipePostF = getIntent().getParcelableExtra("recipe_post");
        Log.d("recipe", recipePostF.toString());
        setContentView(R.layout.activity_lookuprecipe);
        recipetitle = findViewById(R.id.recipetitle_text1);
        recipecategory = findViewById(R.id.recipecate_text1);
        recipespicy = findViewById(R.id.recipespicy_text1);
        recipedescription = findViewById(R.id.recipedescription_text);
        recipecomment = findViewById(R.id.commentcount_text);
        like_btn = findViewById(R.id.like_btn);
        comment_edit = findViewById(R.id.comment_edit);
        comment_add_btn = findViewById(R.id.comment_add_btn);

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
                        return true;
                    case 1:
                        //게시글 수정 로직 추가
                        Log.d("recipe", "수정하기 menu click");
                        return true;
                    case 2:
                        //쪽지보내기 로직 추가
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
        });//사용자에 따른 옵션 메뉴 로직 끝

        //responseCode = 0;

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
        recipespicy.setText("X" + String.valueOf(recipePostF.getDegree_of_spicy()));
        recipedescription.setText(recipePostF.getDescription());
        recipecomment.setText("댓글 " + String.valueOf(recipePostF.getComment_count() + "개"));
        commentAdapter.setCommentList(recipePostF.getComments());
        recipeIngreAdapter.setRecipeIngreList(recipePostF.getRecipe_Ingredients());
        Log.d("recipe", String.valueOf(recipeIngreAdapter.getItemCount()));

        //댓글리스트에 있는 버튼들의 클릭 이벤트 처리
        commentAdapter.setOnItemLeftButtonClickListener(new CommentAdapter.OnItemLeftButtonClickListener() {
            @Override
            public void onItemLeftButtonClick(View view, int position) {
                Log.d("recipe", String.valueOf(position) + "left button click");
                comment_edit.setText(recipePostF.getComments().get(position).getComments());
                comment_add_btn.setText("수정");
                comment_add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment comment = new Comment(recipePostF.getComments().get(position).getComment_id(), ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id(), comment_edit.getText().toString(), "");
                        comment_add_btn.setText("등록");
                        comment_edit.setText("");
                        /* #34 레시피 게시판 댓글 수정 */
                        //Comment comment = new Comment(8, "android fix", 28, "android fix", "");
                        //ccf.editComment(comment);
                    }
                });
                /* #34 레시피 게시판 댓글 수정 */
                //Comment comment = new Comment(8, "android fix", 28, "android fix", "");
                //ccf.editComment(comment);
            }
        });
        commentAdapter.setOnItemRightButtonClickListener(new CommentAdapter.OnItemRightButtonClickListener() {
            @Override
            public void onItemRightButtonClick(View view, int position) {
                Log.d("recipe", String.valueOf(position) + "right button click");
                ControlComment_f ccf = new ControlComment_f();
                //로그인한 사용자와 댓글을 단 사용자와 같은 경우는 삭제 로직
                if (Objects.equals(ControlLogin_f.userinfo.getNickname(), recipePostF.getComments().get(position).getNickname())) {
                    ccf.deleteComment(ControlLogin_f.userinfo.getNickname(), recipePostF.getComments().get(position).getComment_id());
                } else {//같지 않은 경우는 신고 로직
                    Intent intent = new Intent(RecipeLookupActivity.this, ReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("comment_id", recipePostF.getComments().get(position).getComment_id());
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

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //좋아요 관련 로직 추가

                //게시글 아이디로 좋아요 판단 여부가 있으면 좋겠습니다.
                /* #27 레시피 게시글 "좋아요" 등록 */
                //clf.likePost("test", 1, 28);

                /* #27 레시피 게시글 "좋아요" 취소 */
                //clf.unLikePost("test", 1, 28);
            }
        });

        comment_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //댓글 등록 관련 로직 추가
                //새로고침필수
                /* #33 레시피 게시판 댓글 작성 */
                Comment comment = new Comment(0, ControlLogin_f.userinfo.getNickname(), recipePostF.getPost_id(), comment_edit.getText().toString(), "");
                comment_edit.setText("");
                //ccf.writeComment(comment);
            }
        });


        //쓰레드로 요청해서 받는 방식. 초기화면 구성때는 오래걸려서 그냥 intent로 받아옴. 나중에 새로고침 필요할때 사용할 것
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (responseCode == 200) {
//                    responseCode = -1;
//                    recipetitle.setText(recipePost.getTitle());
//                    recipecategory.setText(recipePost.getCategory());
//                    recipespicy.setText("X" + String.valueOf(recipePost.getDegree_of_spicy()));
//                    recipedescription.setText(recipePost.getDescription());
//
//                } else if (responseCode == 500) {
////                    rlu.startDialog(0,"서버 오류","서버 연결에 실패하였습니다.",new ArrayList<>(Arrays.asList("확인")));
//                } else if (responseCode == 502) {
////                    rlu.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
//                }
//            }
//        };
//
//        class NewRunnable implements Runnable {
//            @Override
//            public void run() {
//                for (int i = 0; i < 30; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(runnable);
//                }
//            }
//        }
//        ControlRecipe_f crf = new ControlRecipe_f();
//        crf.lookupRecipe(recipePost.getPost_id());
//        NewRunnable nr = new NewRunnable();
//        Thread t = new Thread(nr);
//        t.start();
    }

    class RecipeLookup_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO 메시지가 표시되는 위치지정 (가운데 표시)
            //toast.setGravity(Gravity.TOP, 0, 0); //TODO 메시지가 표시되는 위치지정 (상단 표시)
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

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RecipeLookupActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                // 회원가입에서 요청한 이메일 인증
                EmailVerificationActivity.destination = 0;
                Intent intent = new Intent(RecipeLookupActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }
    }
}