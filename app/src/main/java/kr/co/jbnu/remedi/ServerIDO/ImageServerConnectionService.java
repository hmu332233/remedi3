package kr.co.jbnu.remedi.serverIDO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public interface ImageServerConnectionService {

    @Multipart
    @POST("/viate/storeimg")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

}
