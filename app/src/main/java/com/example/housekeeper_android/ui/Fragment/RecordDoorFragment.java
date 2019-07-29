package com.example.housekeeper_android.ui.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.DoorRecordRVAdapter;
import com.example.housekeeper_android.ui.Adapter.RecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetDoorRecordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordDoorFragment extends Fragment {

    RecyclerView rvDoorRecord;
    DoorRecordRVAdapter doorRecordRVAdapter;

    public RecordDoorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_record_door, container, false);

        rvDoorRecord = (RecyclerView)rootView.findViewById(R.id.rvDoorRecord);

        Call<GetDoorRecordResponse> getDoorRecordResponseCall = ApplicationController.getInstance().getNetworkService().getDoorRecordResponse();
        getDoorRecordResponseCall.enqueue(new Callback<GetDoorRecordResponse>() {
            @Override
            public void onResponse(Call<GetDoorRecordResponse> call, Response<GetDoorRecordResponse> response) {
                Log.d("RESPONSE_TEST",response.body().result.get(0).toString());
                doorRecordRVAdapter = new DoorRecordRVAdapter(rootView.getContext(),response.body().result);
                rvDoorRecord.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
                rvDoorRecord.setHasFixedSize(true);
                rvDoorRecord.setAdapter(doorRecordRVAdapter);
            }

            @Override
            public void onFailure(Call<GetDoorRecordResponse> call, Throwable t) {
                Toast.makeText(getContext(),"FAIL",Toast.LENGTH_SHORT);
            }
        });


        return rootView;


    }

}


