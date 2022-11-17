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


//public class RecipeList
//{
//    private RecipeList[] recipeList;
//
//    private String total_page;
//
//    public RecipeList[] getRecipeList ()
//    {
//        return recipeList;
//    }
//
//    public void setRecipeList (RecipeList[] recipeList)
//    {
//        this.recipeList = recipeList;
//    }
//
//    public String getTotal_page ()
//    {
//        return total_page;
//    }
//
//    public void setTotal_page (String total_page)
//    {
//        this.total_page = total_page;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "ClassPojo [recipeList = "+recipeList+", total_page = "+total_page+"]";
//    }
//}