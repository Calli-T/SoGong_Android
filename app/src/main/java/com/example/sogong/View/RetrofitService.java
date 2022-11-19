package com.example.sogong.View;

import com.example.sogong.Model.AuthInfo;
import com.example.sogong.Model.LikeInfo;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Report;
import com.example.sogong.Model.SearchInfo;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.Model.User;
import com.example.sogong.Model.PhotoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {
    /*
    @FormUrlEncoded
    @POST("login/")
    Call<User> Login(
            @Field("uid") String uid,
            @Field("nickname") String nickname,
            @Field("password") String password,
            @Field("email") String email,
            @Field("auto_login") boolean auto_login
    );
    */
    @POST("signup/{post}")
    Call<User> setSignUp(@Body User user);

    @POST("login/")
    Call<User> Login(@Body User user);

    @POST("firstcheck/")
    Call<String> AuthStart(@Body AuthInfo authInfo);

    @POST("secondcheck/")
    Call<String> AuthFinish(@Body AuthInfo authInfo);

    @POST("signup/")
    Call<String> SignUp(@Body User user);

    @POST("changepw/")
    Call<String> EditPassword(@Body User user);

    @POST("changenickname/")
    Call<String> EditNickname(@Body User user);

    @POST("logout/")
    Call<String> Logout(@Body User user);

    //-----------------------------------------------------------case 17 ~

    @GET("photoboard/{page}/")
    Call<PhotoList> LookupPhotoList(@Path("page") int page);

    @GET("photo/{postid}/")
    Call<PhotoPost> LookupPhoto(@Path("postid") int postid);

    @POST("uploadphoto/")
    Call<PhotoPost> AddPhoto(@Body PhotoPost photoPost);

    @HTTP(method = "DELETE", path = "https://recippe-sg.herokuapp.com/deletephoto/", hasBody = true)
    Call<Integer> DeletePhoto(@Body PhotoPost deletePhoto);

    @POST("sortphoto/")
    Call<List<PhotoPost>> SortPhotoList(@Body SortInfo sortInfo);

    @POST("likephoto/")
    Call<Integer> UnLikePhotoPost(@Body LikeInfo likeInfo);

    @POST("likephoto/")
    Call<Integer> LikePhotoPost(@Body LikeInfo likeInfo);

    @POST("reportphoto/")
    Call<Integer> ReportPhotoPost(@Body Report reportInfo);

    //-----------------------------------------------------------case 23 ~

    @GET("recipeboard/{page}/")
    Call<RecipeList> LookupRecipeList(@Path("page") int page);

    @GET("recipe/{postId}")
    Call<RecipePost_f> LookupRecipe(@Path("postId") int postId);

    @POST("uploadrecipe/")
    Call<RecipePost_f> AddRecipe(@Body RecipePost_f recipePostF);

    @POST("updaterecipe/")
    Call<RecipePost_f> EditRecipe(@Body RecipePost_f recipePostF);

    @POST("deleterecipe/")
    Call<Integer> DeleteRecipe(@Body RecipePost_f targetPost);

    @POST("likerecipe/")
    Call<Integer> LikeRecipePost(@Body LikeInfo likeInfo);

    @POST("likerecipe/")
    Call<Integer> UnLikeRecipePost(@Body LikeInfo likeInfo);

    @POST("queryrecipe/")
    Call<RecipeList> SearchRecipeList(@Body SearchInfo searchInfo);

    @POST("sortrecipe/")
    Call<List<RecipePost_f>> SortRecipeList(@Body SortInfo sortInfo);

    @POST("reportrecipe/")
    Call<Integer> ReportRecipePost(@Body Report reportInfo);

    @GET("unexistingredients/{nickname}/{post_id}/")
    Call<List<Recipe_Ingredients>> LookupUnExistIngredients(@Path("nickname") String nickname, @Path("post_id") int post_id);

    @GET("decreaseamount/{nickname}/{post_id}/")
    Call<Integer> RemainAmmounts(@Path("nickname") String nickname, @Path("post_id") int post_id);

    //------------------------------------------------------------case 33 ~

    /*
    {
	"like_id": 0 or null,
	"nickname": 시스템을 사용중인 사용자 닉네임,
	"postType": 게시글 종류 1==레시피, -1==사진,
	"postId": 게시글 아이디,
	"task": "취소" or "등록" -> "좋아요"를 취소할지 등록할지 정하는 것
}
     */
    //@POST("likerecipe/")
    //Call<String>

    //@GET("recipeboard/{page}")
    //Call<String>
    //사용자 보유 재료 조회
    @GET("/inquiryrefrigerator/{nickname}/")
    Call<Recipe_Ingredients> LookupIngredientsList(@Path("nickname") String nickname);

}
/*
    // @GET( EndPoint-자원위치(URI) )
    //가져오기
    @GET("board/{post}")
    Call<PostObject> getPost(@Path("post") String post);

    @GET("board/")
    Call<List<PostObject>> getAllPosts();

    @GET("login/{uid}")
    Call<User> getLogin(@Path("uid") String uid);


    //등록
    @POST("board/")
    Call<PostObject> setPostBody(@Body PostObject post);

    @POST("login/")
    Call<User> setLoginBody(@Body User user);

//    @FormUrlEncoded
//    @POST("todo")
//    Call<PostSending> setPostField(
//            @Field("title") String title,
//            @Field("description") String description,
//            @Field("important") boolean important
//
//    );

    //수정
    @PUT("board/{id}")
    Call<PostObject> getPutBody(
            @Path("id") String id,
            @Body PostObject PostObject
    );

    //삭제
    @DELETE("board/{id}")
    Call<Void> deletePost(@Path("id") String id);
 */
