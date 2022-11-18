package com.example.sogong.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.Ingredients;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Recipe_Ingre_Adapter extends RecyclerView.Adapter<Recipe_Ingre_Adapter.ViewHolder> {

    private List<Ingredients> mData;
    private Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            name = (TextView) itemView.findViewById(R.id.name);
            amount = (TextView) itemView.findViewById(R.id.amount);

        }

        void onBind(Ingredients ingredients) {
            name.setText(ingredients.getName());
            String amountstr = String.valueOf(ingredients.getAmmount()) + ingredients.getUnit();
            Log.d("amount", "재료 및 양 " + amountstr);
            amount.setText(amountstr);

        }

        public TextView getName() {
            return name;
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public Recipe_Ingre_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_recipe_ingre, parent, false);
        Recipe_Ingre_Adapter.ViewHolder vh = new Recipe_Ingre_Adapter.ViewHolder(view);

        //리사이클러 클릭 이벤트
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String data = "";
//                int position = vh.getAbsoluteAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    data = vh.getAuthor().getText().toString();
//                }
//                itemClickListener.onItemClicked(position, data);
//
//            }
//        });

        return vh;
    }

    public void setRecipeIngreList(List<Ingredients> list) {
        this.mData = list;
        notifyDataSetChanged();

    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(Recipe_Ingre_Adapter.ViewHolder holder, int position) {
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