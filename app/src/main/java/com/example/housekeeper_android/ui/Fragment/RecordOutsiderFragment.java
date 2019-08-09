package com.example.housekeeper_android.ui.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.DoorRecordRVAdapter;
import com.example.housekeeper_android.ui.Adapter.OutsiderRecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetOutsiderRecordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordOutsiderFragment extends Fragment {
    RecyclerView rvOutsiderRecord;
    OutsiderRecordRVAdapter outsiderRecordRVAdapter;

    public RecordOutsiderFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_record_outsider, container, false);

        rvOutsiderRecord = (RecyclerView)rootView.findViewById(R.id.rvOutsiderRecord);

        Call<GetOutsiderRecordResponse> getOutsiderRecordResponseCall = ApplicationController.getInstance().getNetworkService().getOutsiderRecordResponse();
        getOutsiderRecordResponseCall.enqueue(new Callback<GetOutsiderRecordResponse>() {
            @Override
            public void onResponse(Call<GetOutsiderRecordResponse> call, Response<GetOutsiderRecordResponse> response) {
                outsiderRecordRVAdapter = new OutsiderRecordRVAdapter(rootView.getContext(),response.body().result);
                rvOutsiderRecord.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
                rvOutsiderRecord.setHasFixedSize(true);
                rvOutsiderRecord.setAdapter(outsiderRecordRVAdapter);
            }

            @Override
            public void onFailure(Call<GetOutsiderRecordResponse> call, Throwable t) {

            }
        });



        return rootView;
    }
}
