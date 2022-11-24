package com.example.sogong.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.PhotoLookUp;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePostLookUp;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.List;
import java.util.Objects;

public class PhotoLookupActivity extends AppCompatActivity {
    TextView photoAuthor;
    TextView photoDate;
    TextView photoLike_cnt;

    ImageView photoImage;
    ImageButton menubutton;
    ImageButton like_btn;

    public static int responseCode;
    public static PhotoLookUp photoLookUp;

    PopupMenu dropDownMenu;
    Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookupphoto);
        responseCode = 0;

        RecipeLookup_UI rlu = new RecipeLookup_UI();
        PhotoPost photoPost = getIntent().getParcelableExtra("photo_post");

        photoAuthor = findViewById(R.id.photoauthor_text1);
        photoDate = findViewById(R.id.photodate_text1);
        photoLike_cnt = findViewById(R.id.photolikecnt_text);
        photoImage = findViewById(R.id.photopost);
        like_btn = findViewById(R.id.like_btn);

        //사용자에 따른 옵션 메뉴 로직 시작
        menubutton = findViewById(R.id.menu_button);
        dropDownMenu = new PopupMenu(this, menubutton);
        menu = dropDownMenu.getMenu();
        if (ControlLogin_f.userinfo.getNickname().equals(photoPost.getAuthor())) {
            menu.add(0, 0, 0, "삭제하기");
            //menu.add(0, 1, 0, "수정하기"); 사진은 수정없다!!
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
                        Intent intent1 = new Intent(PhotoLookupActivity.this, MailSendActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.putExtra("mail_receiver", photoPost.getAuthor());
                        startActivity(intent1);
                        Log.d("recipe", "쪽지 보내기 menu click");
                        return true;
                    case 3:
                        //신고하기 로직 추가
                        Intent intent = new Intent(PhotoLookupActivity.this, ReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("recipe_id", photoPost.getPost_id());
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


        //넘겨온 값으로 채워넣음
        photoAuthor.setText(photoPost.getAuthor());
        photoDate.setText(photoPost.getUpload_time());
        photoLike_cnt.setText(String.valueOf(photoPost.getLike_count()));
        byte[] encodeByte = Base64.decode(photoPost.getPhoto_link(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        photoImage.setImageBitmap(bitmap);

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //좋아요 관련 로직 추가

                /* #21 요리 사진 게시글 "좋아요" 취소 */
                //clf.unLikePost("test", -1, 1);

                /* #21 요리 사진 게시글 "좋아요" 등록 */
                //clf.likePost("test", -1, 1);
            }
        });


        //쓰레드로 요청해서 받는 방식. 초기화면 구성때는 오래걸려서 그냥 intent로 받아옴. 나중에 새로고침 필요할때 사용할 것
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;


                } else if (responseCode == 500) {
                    //rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 502) {
                    //rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                    runOnUiThread(runnable);
                }
            }
        }
        //ControlRecipe_f crf = new ControlRecipe_f();
        //crf.lookupRecipe(recipePost.getPost_id());

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(42, "test");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
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
            Custom_Dialog cd = new Custom_Dialog(PhotoLookupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(PhotoLookupActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                // 회원가입에서 요청한 이메일 인증
                EmailVerificationActivity.destination = 0;
                Intent intent = new Intent(PhotoLookupActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }
    }
}
