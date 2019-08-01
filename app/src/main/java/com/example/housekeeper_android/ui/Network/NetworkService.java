package com.example.housekeeper_android.ui.Network;

import com.example.housekeeper_android.ui.Network.Delete.DeleteDoorRecordResponse;
import com.example.housekeeper_android.ui.Network.Delete.DeleteRecordResponse;
import com.example.housekeeper_android.ui.Network.Delete.DeleteWindowRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetDoorRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetRecordListResponse;
import com.example.housekeeper_android.ui.Network.Get.GetWindowRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetWindowStatusResponse;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusRequest;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {

    // 녹음 상용구 가져오기
    @GET("setting/record")
    Call<GetRecordListResponse> getRecordList();

    // 녹음 상용구 삭제
    @DELETE("setting/record/{idx}")
    Call<DeleteRecordResponse> deleteRecord(
            @Path("idx") Integer idx
    );

    // 녹음 상용구 추가
    @Multipart
    @POST("setting/record")
    Call<PostRecordFileResponse> postRecordFile(
            @Part MultipartBody.Part file,
            @Part("fileName") RequestBody fileName
    );

    // 현관 기록 가져오기
    @GET("repository/door")
    Call<GetDoorRecordResponse> getDoorRecordResponse();

    // 창문 기록 가져오기
    @GET("repository/window")
    Call<GetWindowRecordResponse> getWindowRecordResponse();

    // 창문 기록 삭제
    @DELETE("repository/window/{idx}")
    Call<DeleteWindowRecordResponse> deleteWindowRecordResponse(
            @Path("idx") Integer idx
    );

    // 현관문 기록 삭제
    @DELETE("repository/door/{idx}")
    Call<DeleteDoorRecordResponse> deleteDoorRecordResponse(
            @Path("idx") Integer idx
    );

    @GET("window/status")
    Call<GetWindowStatusResponse> getWindowStatus();

    @POST("window/status")
    Call<PostWindowStatusResponse> postWindowStatus(
            @Body PostWindowStatusRequest postWindowStatusRequest
    );
}
