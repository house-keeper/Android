package com.example.housekeeper_android.ui.Network;

import com.example.housekeeper_android.ui.Network.Get.GetWindowStatusResponse;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService {


    // 녹음 상용구 추가
    @Multipart
    @POST("setting/record")
    Call<PostRecordFileResponse> postRecordFile(
            @Part MultipartBody.Part file,
            @Part("fileName") RequestBody fileName);

    @GET("window/status")
    Call<GetWindowStatusResponse> getWindowStatus(

    );

    @POST("window/status")
    Call<PostWindowStatusResponse> postWindowStatus(

    );
}
