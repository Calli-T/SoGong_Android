package com.example.sogong.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Model.RecipePost;
import com.example.sogong.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<RecipePost> mData = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView spicy;
        TextView date;
        TextView author;
        TextView likecnt;
        TextView commentcnt;
        TextView viewcnt;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title);
            spicy = itemView.findViewById(R.id.spicy);
            date = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
            likecnt = itemView.findViewById(R.id.likecount_text);
            commentcnt = itemView.findViewById(R.id.commentcount_text);
            viewcnt = itemView.findViewById(R.id.viewcount_text);
        }

        void onBind(RecipePost recipePost) {
            title.setText(recipePost.getTitle());
            spicy.setText(recipePost.getDegree_of_spicy());
            date.setText(recipePost.getUpload_time());
            author.setText(recipePost.getNickname());
            likecnt.setText(recipePost.getLike_count());
            commentcnt.setText(recipePost.getComment_count());
            viewcnt.setText(recipePost.getViews());
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecipeAdapter(ArrayList<RecipePost> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_recipepostlist, parent, false);
        RecipeAdapter.ViewHolder vh = new RecipeAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
}