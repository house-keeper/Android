package com.example.housekeeper_android.ui.Network;

import com.example.housekeeper_android.ui.Network.Get.GetWindowStatusResponse;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusRequest;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {
    @Multipart
    @POST("setting/record")
    Call<PostRecordFileResponse> postRecordFile(
            @Part MultipartBody.Part file);

    @GET("window/status")
    Call<GetWindowStatusResponse> getWindowStatus(
    );

    @POST("window/status")
    Call<PostWindowStatusResponse> postWindowStatus(
            @Body PostWindowStatusRequest postWindowStatusRequest
    );
}
