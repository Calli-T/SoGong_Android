package com.example.sogong.View;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipePost_f> mData;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView pepper1;
        ImageView pepper2;
        ImageView pepper3;
        ImageView pepper4;
        ImageView pepper5;
        TextView date;
        TextView author;
        TextView likecnt;
        TextView commentcnt;
        TextView viewcnt;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title);
            pepper1 = itemView.findViewById(R.id.pepper_1);
            pepper2 = itemView.findViewById(R.id.pepper_2);
            pepper3 = itemView.findViewById(R.id.pepper_3);
            pepper4 = itemView.findViewById(R.id.pepper_4);
            pepper5 = itemView.findViewById(R.id.pepper_5);
            date = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
            likecnt = itemView.findViewById(R.id.likecount_text);
            commentcnt = itemView.findViewById(R.id.commentcount_text);
            viewcnt = itemView.findViewById(R.id.viewcount_text);
        }

        void onBind(RecipePost_f recipePostF) {
            title.setText(recipePostF.getTitle());
            switch (recipePostF.getDegree_of_spicy()) {
                case 0:
                    pepper1.setVisibility(View.INVISIBLE);
                    pepper2.setVisibility(View.INVISIBLE);
                    pepper3.setVisibility(View.INVISIBLE);
                    pepper4.setVisibility(View.INVISIBLE);
                    pepper5.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    pepper1.setVisibility(View.VISIBLE);
                    pepper2.setVisibility(View.INVISIBLE);
                    pepper3.setVisibility(View.INVISIBLE);
                    pepper4.setVisibility(View.INVISIBLE);
                    pepper5.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    pepper1.setVisibility(View.VISIBLE);
                    pepper2.setVisibility(View.VISIBLE);
                    pepper3.setVisibility(View.INVISIBLE);
                    pepper4.setVisibility(View.INVISIBLE);
                    pepper5.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    pepper1.setVisibility(View.VISIBLE);
                    pepper2.setVisibility(View.VISIBLE);
                    pepper3.setVisibility(View.VISIBLE);
                    pepper4.setVisibility(View.INVISIBLE);
                    pepper5.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    pepper1.setVisibility(View.VISIBLE);
                    pepper2.setVisibility(View.VISIBLE);
                    pepper3.setVisibility(View.VISIBLE);
                    pepper4.setVisibility(View.VISIBLE);
                    pepper5.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    pepper1.setVisibility(View.VISIBLE);
                    pepper2.setVisibility(View.VISIBLE);
                    pepper3.setVisibility(View.VISIBLE);
                    pepper4.setVisibility(View.VISIBLE);
                    pepper5.setVisibility(View.VISIBLE);
                    break;

            }
            String time = recipePostF.getUpload_time();
            String[] time1 = time.split("T");
            date.setText(time1[0]);
            author.setText(recipePostF.getNickname());
            likecnt.setText(String.valueOf(recipePostF.getLike_count()));
            commentcnt.setText(String.valueOf(recipePostF.getComment_count()));
            viewcnt.setText(String.valueOf(recipePostF.getViews()));
        }

        public TextView getTitle() {
            return title;
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_recipepostlist, parent, false);
        RecipeAdapter.ViewHolder vh = new RecipeAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                int position = vh.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data = vh.getTitle().getText().toString();
                }
                itemClickListener.onItemClicked(position, data);

            }
        });

        return vh;
    }

    public void setRecipeList(List<RecipePost_f> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        else return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }


}