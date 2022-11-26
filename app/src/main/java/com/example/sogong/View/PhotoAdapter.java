package com.example.sogong.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.List;

/* 그리드뷰 어댑터 */
class PhotoAdapter extends BaseAdapter {
    List<PhotoPost> mData = new ArrayList<PhotoPost>();
    public ImageView ivIcon;

    public TextView tvName;

    @Override
    public int getCount() {
        return mData.size();
    }

    public void setPhotoList(List<PhotoPost> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void addItem(PhotoPost item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final PhotoPost photoPost = mData.get(position);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView photo;

            convertView = inflater.inflate(R.layout.grid_photo, viewGroup, false);
            photo = (ImageView) convertView.findViewById(R.id.photo_item);

            byte[] encodeByte = Base64.decode(photoPost.getPhoto_link(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            photo.setImageBitmap(bitmap);
        } else {
            View view = new View(context);
            view = (View) convertView;
        }

//        //각 아이템 선택 event
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("grid", photoPost.getPost_id() + " 클릭");
//
//
//            }
//        });

        return convertView;  //뷰 객체 반환
    }

}