package com.example.housekeeper_android.ui.Network;

import android.database.Observable;

import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService {

    @Multipart
    @POST("업로드 서버 주소")
    Call<PostRecordFileResponse> postRecordFile(
            @Part MultipartBody.Part file );

}
