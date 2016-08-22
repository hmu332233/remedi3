package kr.co.jbnu.remedi.serverIDO;

import java.util.ArrayList;

import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public interface ServerConnectionService {
    @FormUrlEncoded
    @POST("/mobile/login_request")
    Call<User> login_request(@Field("email") String ID, @Field("pw") String PW);

    @FormUrlEncoded
    @POST("/mobile/user_join")
    Call<Void> user_join(@Field("email") String email, @Field("pw") String pw, @Field("name") String name, @Field("user_type") String type, @Field("register_id") String register_id);

    @FormUrlEncoded
    @POST("/mobile/check_exist_user")
    Call<Boolean> check_exist_user(@Field("email") String email);

    @FormUrlEncoded
    @POST("/mobile/update_gcm_register_id")
    Call<Void> update_gcm_register_id(@Field("email") String email,@Field("reg_id") String reg_id);

    @FormUrlEncoded
    @POST("/mobile/get_normaluser_board")
    Call<ArrayList<Board>> get_normaluser_board(@Field("email") String email);

    @FormUrlEncoded
    @POST("/mobile/get_pharmacist_normalboard")
    Call<ArrayList<Board>> get_pharmacist_normalboard();

    @FormUrlEncoded
    @POST("/mobile/get_pharmacist_writtenboard")
    Call<ArrayList<Board>> get_pharmacist_writtenboard(@Field("id_string") String id);



    @FormUrlEncoded
    @POST("/mobile/get_detail_board")
    Call<Board> get_detail_board(@Field("Board_ID") String Board_ID);

    @FormUrlEncoded
    @POST("/mobile/change_profile_img")
    String change_profile_img(@Field("ID") String ID);


}
