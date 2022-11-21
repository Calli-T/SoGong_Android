package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class RecipePostLookUp {
    @SerializedName("recipeInfo")
    private RecipePost_f recipeInfo;

    @SerializedName("likeInfo")
    private boolean likeInfo;

    public RecipePostLookUp(RecipePost_f recipeInfo, boolean likeInfo) {
        this.recipeInfo = recipeInfo;
        this.likeInfo = likeInfo;
    }

    public RecipePost_f getRecipeInfo() {
        return recipeInfo;
    }

    public void setRecipeInfo(RecipePost_f recipeInfo) {
        this.recipeInfo = recipeInfo;
    }

    public boolean isLikeInfo() {
        return likeInfo;
    }

    public void setLikeInfo(boolean likeInfo) {
        this.likeInfo = likeInfo;
    }

    @Override
    public String toString() {
        return "RecipePostLookUp{" +
                "recipeInfo=" + recipeInfo +
                ", likeInfo=" + likeInfo +
                '}';
    }
}
