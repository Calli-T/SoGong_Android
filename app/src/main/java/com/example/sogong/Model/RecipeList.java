package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeList {
    @SerializedName("recipeList")
    private List<RecipePost_f> recipeList;
    @SerializedName("total_page")
    private int total_page;

    public List<RecipePost_f> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<RecipePost_f> recipeList) {
        this.recipeList = recipeList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    @Override
    public String toString() {
        String msg = "";
        for(int i=0; i<getRecipeList().size(); i++) {
            msg += (getRecipeList().get(i).toString() + "\n");
        }
        return msg;
    }
}