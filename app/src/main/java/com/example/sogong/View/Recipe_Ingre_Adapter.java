package com.example.sogong.View;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.List;

public class Recipe_Ingre_Adapter extends RecyclerView.Adapter<Recipe_Ingre_Adapter.ViewHolder> {

    private List<Recipe_Ingredients> mData;
    private ArrayList<Integer> isExist;
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

        void onBind(Recipe_Ingredients ingredients, Boolean exist) {
            name.setText(ingredients.getName());
            String amountstr = String.valueOf(ingredients.getAmount()) + ingredients.getUnit();
            Log.d("amount", "재료 및 양 " + amountstr);
            amount.setText(amountstr);

            // 없는 재료는 붉은 글씨로
            if(!exist){
                name.setTextColor(Color.parseColor("#FF0000"));
                amount.setTextColor(Color.parseColor("#FF0000"));
            }
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

        return vh;
    }

    public void setRecipeIngreList(List<Recipe_Ingredients> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void setRecipeIngreListExist(List<Recipe_Ingredients> list, ArrayList<Integer> isExist) {
        this.mData = list;
        this.isExist = isExist;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(Recipe_Ingre_Adapter.ViewHolder holder, int position) {
        if (isExist == null)
            holder.onBind(mData.get(position), true);
        else{
            // 재료를 가지고 있을 때와 없을 때를 다르게 처리
            if(!isExist.contains(mData.get(position).getId())) {
                Log.d("ingre_adapter", position+ " " + mData.get(position).getId());
                holder.onBind(mData.get(position), true);
            }
            else{
                Log.d("ingre_adapter", position+ " " + mData.get(position).getId());
                holder.onBind(mData.get(position), false);
            }
        }
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