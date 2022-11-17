package com.example.sogong.View;

import com.example.sogong.Model.AuthInfo;
import com.example.sogong.Model.LikeInfo;
import com.example.sogong.Model.Ingredients;
import com.example.sogong.Model.PostObject;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    //-----------------------------------------------------------case 23

    // 레시피 한 개 받아오는거 테스트용
    @GET("recipe/{pid}/")
    Call<RecipePost> test(@Path("pid") int pid);

    // 레시피 한 개 받아노는거 String 테스트
    @GET("recipe/{pid}/")
    Call<String> testString(@Path("pid") int pid);

    // 레시피 게시판 받아오는거 String 테스트
    @GET("recipeboard/{page}/")
    Call<String> listString(@Path("page") int page);

    // 레시피 게시판 받아오는거 String 테스트
    @GET("recipeboard/{page}/")
    Call<RecipeList> LookupRecipeList(@Path("page") int page);

    // 레시피 업로드
    @POST("uploadrecipe/")
    Call<String> AddRecipe(@Body RecipePost recipePost);

    // 레시피 수정
    @POST("updaterecipe/")
    Call<String> EditRecipe(@Body RecipePost recipePost);

    // 레시피 삭젠
    @POST("deleterecipe/")
    Call<String> DeleteRecipe(@Field("nickname") String nickname, @Field("post_id") int post_id);
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
    Call<Ingredients> LookupIngredientsList(@Path("nickname") String nickname);

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
