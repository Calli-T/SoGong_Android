package com.example.sogong.Control;

import com.example.sogong.Model.RecipePost;

import java.util.List;

public class ControlMyRecipe_f {
    List<RecipePost> recipeList;

    public void lookupMyRecipeList(String nickname) {}
    public void searchMyRecipeList(String nickname, String keyword){}
    public void sortMyRecipeList(String nickname, String sortBy){}
    public void showRecipeList(List<RecipePost> recipeList){}
    public void noResult(String message){}

}
