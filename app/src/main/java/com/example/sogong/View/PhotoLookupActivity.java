package com.example.sogong.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.BoringLayout;
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
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.PhotoLookUp;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePostLookUp;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhotoLookupActivity extends AppCompatActivity {
    TextView photoAuthor;
    TextView photoDate;
    TextView photoLike_cnt;

    ImageView photoImage;
    ImageButton menubutton;
    ImageButton like_btn;
    ImageButton back_button;

    Boolean likedState;

    public static int responseCode;
    public static PhotoLookUp photoLookUp;
    private AtomicBoolean threadFlag = new AtomicBoolean();
    PopupMenu dropDownMenu;
    Menu menu;
    Custon_ProgressDialog custon_progressDialog;

    PhotoLookup_UI plu = new PhotoLookup_UI();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookupphoto);
        responseCode = 0;

        PhotoPost photoPost = getIntent().getParcelableExtra("photo_post");

        //???????????? ??????
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        photoAuthor = findViewById(R.id.photoauthor_text1);
        photoDate = findViewById(R.id.photodate_text1);
        photoLike_cnt = findViewById(R.id.photolikecnt_text);
        photoImage = findViewById(R.id.photopost);
        like_btn = findViewById(R.id.like_btn);
        //????????? ??????
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();
        //???????????? ?????? ?????? ?????? ?????? ??????
        menubutton = findViewById(R.id.menu_button);
        dropDownMenu = new PopupMenu(this, menubutton);
        menu = dropDownMenu.getMenu();
        if (ControlLogin_f.userinfo.getNickname().equals(photoPost.getAuthor())) {
            menu.add(0, 0, 0, "????????????");
            //menu.add(0, 1, 0, "????????????"); ????????? ????????????!!
        } else {
            menu.add(0, 2, 0, "?????? ?????????");
            menu.add(0, 3, 0, "????????????");
        }
        //?????? ?????? ?????? ?????????
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        //????????? ?????? ?????? ??????
                        Log.d("recipe", "???????????? menu click");
                        plu.startDialog(1, "?????? ?????? ????????? ??????", "???????????? ?????????????????????????", new ArrayList<>(Arrays.asList("??????", "??????")));
                        class NewRunnable implements Runnable {
                            NewRunnable() {
                            }

                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(100);
                                        if (Custom_Dialog.state == 0) {//????????? ?????? ??????
                                            Custom_Dialog.state = -1;
                                            threadFlag.set(true);
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (responseCode == 200) {
                                                        responseCode = -1;
                                                        threadFlag.set(false);
                                                        Log.d("?????? ??????", "??????");
//                                                        custon_progressDialog.dismiss();
                                                        onBackPressed();
                                                    } else if (responseCode == 500) {
                                                        responseCode = -1;
                                                        threadFlag.set(false);
                                                        Log.d("?????? ??????", "??????");
                                                        // custon_progressDialog.dismiss();
                                                        plu.startDialog(0, "?????? ??????", "????????? ????????? ?????????????????????.", new ArrayList<>(Arrays.asList("??????")));

                                                    } else if (responseCode == 502) {
                                                        responseCode = -1;
                                                        threadFlag.set(false);
                                                        Log.d("?????? ??????", "??????");
                                                        // custon_progressDialog.dismiss();
                                                        plu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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
                                            ControlPhoto_f cpf = new ControlPhoto_f();
                                            cpf.deletePhoto(ControlLogin_f.userinfo.getNickname(), photoPost.getPost_id());
                                            NewRunnable1 nr = new NewRunnable1();
                                            Thread t = new Thread(nr);
                                            t.start();
                                            break;
                                        } else if (Custom_Dialog.state == 1) {//????????? ?????? ??????
                                            Custom_Dialog.state = -1;
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

                        return true;
                    case 1:
                        //????????? ?????? ?????? ??????
                        Log.d("recipe", "???????????? menu click");
                        return true;
                    case 2:
                        //??????????????? ?????? ??????
                        Intent intent1 = new Intent(PhotoLookupActivity.this, MailSendActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.putExtra("mail_receiver", photoPost.getAuthor());
                        startActivity(intent1);
                        Log.d("recipe", "?????? ????????? menu click");
                        return true;
                    case 3:
                        //???????????? ?????? ??????
                        Intent intent = new Intent(PhotoLookupActivity.this, ReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("photo_id", photoPost.getPost_id());
                        intent.putExtra("report_post_type", 2);
                        startActivity(intent);
                        Log.d("recipe", "???????????? menu click");
                        return true;
                }
                return false;
            }
        });

        //???????????? ?????? ?????????
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });

        //????????? ????????? ????????????
        photoAuthor.setText(photoPost.getAuthor());
        photoDate.setText(photoPost.getUpload_time().split("T")[0]);
        photoLike_cnt.setText(String.valueOf(photoPost.getLike_count()));
        byte[] encodeByte = Base64.decode(photoPost.getPhoto_link(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        photoImage.setImageBitmap(bitmap);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    threadFlag.set(false);
                    Log.d("?????? ????????? ??????", "????????? = " + photoLookUp.isLikeInfo());
                    if (photoLookUp.isLikeInfo()) {
                        like_btn.setBackground(getDrawable(R.drawable.thumb_up_fill));
                    } else {
                        like_btn.setBackground(getDrawable(R.drawable.thumb_up));
                    }
                    likedState = photoLookUp.isLikeInfo();
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
                    custon_progressDialog.dismiss();
                } else if (responseCode == 500) {
                    responseCode = -1;
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();
                    plu.startDialog(0, "?????? ??????", "???????????? ???????????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
                } else if (responseCode == 502) {
                    responseCode = -1;
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();
                    plu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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
                    Log.d("??????????????????", "responsecode = " + responseCode);
                    if (threadFlag.get())
                        runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }
        ControlPhoto_f cpf = new ControlPhoto_f();
        cpf.lookupPhoto(photoPost.getPost_id(), ControlLogin_f.userinfo.getNickname());

        threadFlag.set(true);
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    // ???????????? ????????? ????????? ???????????? ????????? ??????
    @Override
    public void onBackPressed() {
        if (likedState != photoLookUp.isLikeInfo()) {
            custon_progressDialog.show();
            if (photoLookUp.isLikeInfo()) {//????????? ????????? ????????? ??????
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            PhotoLookupActivity.super.onBackPressed();
                        } else if (responseCode == 500) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            plu.startDialog(0, "?????? ??????", "\"?????????\" ????????? ?????????????????????.", new ArrayList<>(Arrays.asList("??????")));
                        } else if (responseCode == 502) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            plu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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
                            Log.d("??????????????????", "responsecode = " + responseCode);
                            if (threadFlag.get())
                                runOnUiThread(runnable);
                            else {
                                i = 30;
                            }
                        }
                    }
                }
                ControlLike_f clf = new ControlLike_f();
                clf.unLikePost(ControlLogin_f.userinfo.getNickname(), 2, photoLookUp.getPhotoPost().getPost_id());

                threadFlag.set(true);
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            } else {
                final Runnable runnable = new Runnable() {//????????? ????????? ????????? ?????????
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            PhotoLookupActivity.super.onBackPressed();
                        } else if (responseCode == 501) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            plu.startDialog(0, "?????? ??????", "\"?????????\" ????????? ?????????????????????.", new ArrayList<>(Arrays.asList("??????")));
                        } else if (responseCode == 502) {
                            responseCode = -1;
                            threadFlag.set(false);
                            custon_progressDialog.dismiss();
                            plu.startDialog(0, "?????? ??????", "??? ??? ?????? ???????????????.", new ArrayList<>(Arrays.asList("??????")));
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
                            Log.d("??????????????????", "responsecode = " + responseCode);
                            if (threadFlag.get())
                                runOnUiThread(runnable);
                            else {
                                i = 30;
                            }
                        }
                    }
                }
                ControlLike_f clf = new ControlLike_f();
                clf.likePost(ControlLogin_f.userinfo.getNickname(), 2, photoLookUp.getPhotoPost().getPost_id());
                Log.d("?????????", "????????? ?????? ????????? id = " + photoLookUp.getPhotoPost().getPost_id());

                threadFlag.set(true);
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        } else PhotoLookupActivity.super.onBackPressed();
    }

    class PhotoLookup_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO ???????????? ???????????? ???????????? (?????? ??????)
            toast.setDuration(Toast.LENGTH_SHORT); //????????? ?????? ??????
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(PhotoLookupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(PhotoLookupActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                EmailVerificationActivity.destination = 0;
                Intent intent = new Intent(PhotoLookupActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }
    }
}
