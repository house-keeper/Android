package com.example.housekeeper_android.ui.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.DoorRecordRVAdapter;
import com.example.housekeeper_android.ui.Adapter.WindowRecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetDoorRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetWindowRecordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordWindowFragment extends Fragment {
    RecyclerView rvWindowRecord;
    WindowRecordRVAdapter windowRecordRVAdapter;

    public RecordWindowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_record_window, container, false);

        rvWindowRecord = (RecyclerView)rootView.findViewById(R.id.rvWindowRecord);

        Call<GetWindowRecordResponse> getWindowRecordResponseCall = ApplicationController.getInstance().getNetworkService().getWindowRecordResponse();
        getWindowRecordResponseCall.enqueue(new Callback<GetWindowRecordResponse>() {
            @Override
            public void onResponse(Call<GetWindowRecordResponse> call, Response<GetWindowRecordResponse> response) {
                Log.d("RESPONSE_TEST",response.body().result.get(0).toString());
                windowRecordRVAdapter = new WindowRecordRVAdapter(response.body().result,rootView.getContext());
                rvWindowRecord.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
                rvWindowRecord.setHasFixedSize(true);
                rvWindowRecord.setAdapter(windowRecordRVAdapter);
            }

            @Override
            public void onFailure(Call<GetWindowRecordResponse> call, Throwable t) {
                Toast.makeText(getContext(),"FAIL",Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }

}
