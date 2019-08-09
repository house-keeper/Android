package com.example.housekeeper_android.ui.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.OutsiderDetailRVAdapter;
import com.example.housekeeper_android.ui.Adapter.OutsiderRecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetOutsiderDetailResponse;
import com.example.housekeeper_android.ui.activity.RecordActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutsiderDetailFragment extends Fragment {

    public String name;
    RecyclerView rvOutsiderDetail;
    OutsiderDetailRVAdapter outsiderDetailRVAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_outsider_detail, container, false);

        if(getArguments() != null) name = getArguments().getString("name"); // 전달한 key 값
        else name = "unknown";

        rvOutsiderDetail = (RecyclerView)rootView.findViewById(R.id.rvOutsiderDetail);
        setHasOptionsMenu(true);

        Call<GetOutsiderDetailResponse> getOutsiderDetailResponseCall = ApplicationController.getInstance().getNetworkService().getOutsiderDetailRespnose(name);
        getOutsiderDetailResponseCall.enqueue(new Callback<GetOutsiderDetailResponse>() {
            @Override
            public void onResponse(Call<GetOutsiderDetailResponse> call, Response<GetOutsiderDetailResponse> response) {
                outsiderDetailRVAdapter = new OutsiderDetailRVAdapter(rootView.getContext(),response.body().result);
                rvOutsiderDetail.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
                rvOutsiderDetail.setHasFixedSize(true);
                rvOutsiderDetail.setAdapter(outsiderDetailRVAdapter);
            }

            @Override
            public void onFailure(Call<GetOutsiderDetailResponse> call, Throwable t) {

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.record_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.record_detail_menu_delete:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        getContext());
                alertBuilder.setTitle("삭제하려면 기록을 길게 누르세요. ");
                alertBuilder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder.show();
                break;

            case R.id.record_detail_menu_move:
                AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(
                        getContext());
                alertBuilder2.setTitle("외부인 이름을 변경하려면 해당하는 기록을 클릭하세요. ");
                alertBuilder2.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
