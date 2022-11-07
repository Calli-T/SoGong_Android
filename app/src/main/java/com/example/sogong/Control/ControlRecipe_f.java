package com.example.sogong.Control;

import com.example.sogong.Model.RecipePost;

public class ControlRecipe_f {
    RecipePost recipePost;
    public void lookupRecipe(int postId){}
    public void addRecipe(RecipePost newRecipe){}
    public void editRecipe(RecipePost edittedRecipe){}
    public void deleteRecipe(String nickname, int postId){}
    public void showRecipe(RecipePost recipePost){}
    public Boolean isMine(String nickname){
        Boolean result = true;
        return result;
    }
}
