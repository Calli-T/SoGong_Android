package com.example.sogong.Control;

import com.example.sogong.Model.RecipePost_f;

import java.util.List;

public class ControlMyRecipe_f {
    List<RecipePost_f> recipeList;

    public void lookupMyRecipeList(String nickname) {}
    public void searchMyRecipeList(String nickname, String keyword){}
    public void sortMyRecipeList(String nickname, String sortBy){}
    public void showRecipeList(List<RecipePost_f> recipeList){}
    public void noResult(String message){}

}
