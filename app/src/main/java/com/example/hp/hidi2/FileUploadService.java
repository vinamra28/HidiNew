package com.example.hp.hidi2;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by HP on 23-Feb-18.
 */

public interface FileUploadService
{
    @Multipart
    @POST("uploadprofile.php")
    Call<ResponseBody> upload(
            @Part("profile") RequestBody description,
            @Part MultipartBody.Part file,
            @Part("uid") RequestBody id
    );
    @Multipart
    @POST("uploadprofile.php")
    Call<ResponseBody> upload1(
            @Part("pic") RequestBody description,
            @Part MultipartBody.Part file,
            @Part("pid") RequestBody pid
    );
    // previous code for single description
//    @Multipart
//    @POST("uploadprofile.php")
//    Call<ResponseBody> uploadFile(
//            @Part("profile") RequestBody description,
//            @Part MultipartBody.Part file);
//
//    @Multipart
//    @POST("uploadprofile.php")
//    Call<ResponseBody> upload(
//            @PartMap() Map<String, RequestBody> partMap,
//            @Part MultipartBody.Part file);
}