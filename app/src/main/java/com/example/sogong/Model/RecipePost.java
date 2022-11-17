package com.example.sogong.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipePost {
    @SerializedName("post_id")
    @Expose
    private int post_id;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("degree_of_spicy")
    @Expose
    private int degree_of_spicy;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("views")
    @Expose
    private int views;

    @SerializedName("like_count")
    @Expose
    private int like_count;

    @SerializedName("comment_count")
    @Expose
    private int comment_count;

    @SerializedName("upload_time")
    @Expose
    private String upload_time;

    @SerializedName("Recipe_Ingredients")
    @Expose
    private List<RecipePost> Recipe_Ingredients;

    @SerializedName("comments")
    @Expose
    private List<Comment> comments;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDegree_of_spicy() {
        return degree_of_spicy;
    }

    public void setDegree_of_spicy(int degree_of_spicy) {
        this.degree_of_spicy = degree_of_spicy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public List<RecipePost> getRecipe_Ingredients() {
        return Recipe_Ingredients;
    }

    public void setRecipe_Ingredients(List<RecipePost> recipe_Ingredients) {
        Recipe_Ingredients = recipe_Ingredients;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "RecipePost{" +
                "post_id=" + post_id +
                ", nickname='" + nickname + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", degree_of_spicy=" + degree_of_spicy +
                ", description='" + description + '\'' +
                ", views=" + views +
                ", like_count=" + like_count +
                ", comment_count=" + comment_count +
                ", upload_time='" + upload_time + '\'' +
                ", Recipe_Ingredients=" + Recipe_Ingredients +
                ", comments=" + comments +
                '}';
    }
}

/*
{
	"post_id": 19,
	"nickname": "test",
	"title": "test0",
	"category": "test0",
	"degree_of_spicy": 1,
	"description": "test0",
	"views": 6,
	"like_count": 1,
	"comment_count": 0,
	"upload_time": "2022-11-14T21:28:04.029119+09:00",
	"Recipe_Ingredients": [
		{
			"id": 18,
			"name": "asparagus",
			"post_id": 19,
			"unit": "T",
			"amount": 30.0
		},
		{
			"id": 19,
			"name": "kkochu",
			"post_id": 19,
			"unit": "kg",
			"amount": 30.0
		}
	],
	"comments": [
		{
			"comment_id": 2,
			"nickname": "test",
			"post_id": 19,
			"comments": "test",
			"comment_time": "2000-01-01T09:00:00+09:00"
		}
	]
}
 */

//public class RecipeList
//{
//    private String comment_count;
//
//    private Recipe_Ingredients[] Recipe_Ingredients;
//
//    private String upload_time;
//
//    private String like_count;
//
//    private Comments[] comments;
//
//    private String post_id;
//
//    private String nickname;
//
//    private String description;
//
//    private String degree_of_spicy;
//
//    private String title;
//
//    private String category;
//
//    private String views;
//
//    public String getComment_count ()
//    {
//        return comment_count;
//    }
//
//    public void setComment_count (String comment_count)
//    {
//        this.comment_count = comment_count;
//    }
//
//    public Recipe_Ingredients[] getRecipe_Ingredients ()
//    {
//        return Recipe_Ingredients;
//    }
//
//    public void setRecipe_Ingredients (Recipe_Ingredients[] Recipe_Ingredients)
//    {
//        this.Recipe_Ingredients = Recipe_Ingredients;
//    }
//
//    public String getUpload_time ()
//    {
//        return upload_time;
//    }
//
//    public void setUpload_time (String upload_time)
//    {
//        this.upload_time = upload_time;
//    }
//
//    public String getLike_count ()
//    {
//        return like_count;
//    }
//
//    public void setLike_count (String like_count)
//    {
//        this.like_count = like_count;
//    }
//
//    public Comments[] getComments ()
//    {
//        return comments;
//    }
//
//    public void setComments (Comments[] comments)
//    {
//        this.comments = comments;
//    }
//
//    public String getPost_id ()
//    {
//        return post_id;
//    }
//
//    public void setPost_id (String post_id)
//    {
//        this.post_id = post_id;
//    }
//
//    public String getNickname ()
//    {
//        return nickname;
//    }
//
//    public void setNickname (String nickname)
//    {
//        this.nickname = nickname;
//    }
//
//    public String getDescription ()
//    {
//        return description;
//    }
//
//    public void setDescription (String description)
//    {
//        this.description = description;
//    }
//
//    public String getDegree_of_spicy ()
//    {
//        return degree_of_spicy;
//    }
//
//    public void setDegree_of_spicy (String degree_of_spicy)
//    {
//        this.degree_of_spicy = degree_of_spicy;
//    }
//
//    public String getTitle ()
//    {
//        return title;
//    }
//
//    public void setTitle (String title)
//    {
//        this.title = title;
//    }
//
//    public String getCategory ()
//    {
//        return category;
//    }
//
//    public void setCategory (String category)
//    {
//        this.category = category;
//    }
//
//    public String getViews ()
//    {
//        return views;
//    }
//
//    public void setViews (String views)
//    {
//        this.views = views;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "ClassPojo [comment_count = "+comment_count+", Recipe_Ingredients = "+Recipe_Ingredients+", upload_time = "+upload_time+", like_count = "+like_count+", comments = "+comments+", post_id = "+post_id+", nickname = "+nickname+", description = "+description+", degree_of_spicy = "+degree_of_spicy+", title = "+title+", category = "+category+", views = "+views+"]";
//    }
//}