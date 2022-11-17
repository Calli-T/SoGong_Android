package com.example.sogong.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeList {
    @SerializedName("recipeList")
    @Expose
    private List<RecipePost> recipeList;

    @SerializedName("total_page")
    @Expose
    private int total_page;

    public List<RecipePost> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<RecipePost> recipeList) {
        this.recipeList = recipeList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
