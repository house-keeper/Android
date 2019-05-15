package com.example.housekeeper_android.ui.Network;

import android.database.Observable;

import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService {
/*
    @Multipart
    @POST("s3/upload")
    Call<PostRecordFileResponse> postRecordFile(
            @Part("file")  RequestBody file);
*/


    @Multipart
    @POST("s3/upload")
    Call<PostRecordFileResponse> postRecordFile(
            @Part MultipartBody.Part file);

}
