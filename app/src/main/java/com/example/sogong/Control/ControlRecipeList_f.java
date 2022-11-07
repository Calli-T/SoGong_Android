package com.example.sogong.Control;

import com.example.sogong.Model.RecipePost;

import java.util.List;

public class ControlRecipeList_f {
    List<RecipePost> recipeList;
    public void lookupRecipeList(int page){}
    public void searchRecipeList(String searchType, String categories, String keywordType, String keyword, int page){}
    public void sortRecipeList(String sortBy, int page){}
    public void showList(List<RecipePost> recipeList){}
    public void noResult(String message){}
}
