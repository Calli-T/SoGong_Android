package com.example.sogong.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class PhotoAddActivity extends AppCompatActivity {

    public static int responseCode = 0;

    int state;
    Uri uri;
    ImageView imageView;
    Button reselctPhoto;
    Button addPhoto;
    String base64Img;
    private boolean threadFlag; // 프래그먼트 전환에서 스레드를 잠재울 플래그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        state = -1;
        imageView = findViewById(R.id.photo_image);
        reselctPhoto = findViewById(R.id.reselect_photo);
        addPhoto = findViewById(R.id.photo_add_button);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityResult.launch(intent);

        reselctPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    Log.d("photoadd", "이미지 있는 상태");
                    Log.d("photoadd",""+base64Img.length());
                    Log.d("photoadd",""+base64Img.substring(0,100));

                    PhotoPost newPhoto = new PhotoPost(ControlLogin_f.userinfo.getNickname(), 0, base64Img, 0, "");
                    ControlPhoto_f cpf = new ControlPhoto_f();
                    cpf.addPhoto(newPhoto);
                } else Log.d("photoadd", "이미지 없는 상태");
            }
        });

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    Log.d("photoadd","responseCode = "+responseCode );
                }else if(responseCode == 500){
                    Log.d("photoadd","responseCode = "+responseCode );
                }else if(responseCode == 502){
                    Log.d("photoadd","responseCode = "+responseCode );
                }else {
                    Log.d("photoadd","responseCode = "+responseCode );
                }
                // UI 코드 작성해주세요

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
                    if (threadFlag)
                      runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }


            }
        }


        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                            byte[] bytes = baos.toByteArray();
                            base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
                            Log.d("photo_add", base64Img.substring(0, 10));
                            imageView.setBackground(null);
                            imageView.setImageBitmap(bitmap);
                            state = 1;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }/*w  w w. ja  va 2s.c om*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("UTF-8");
    }

    class PhotoAdd_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(PhotoAddActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            /*if (dest == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }*/
        }
    }
}
