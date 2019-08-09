package com.example.housekeeper_android.ui.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Data.OutsiderDetailData;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Delete.DeleteOutsiderRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetOutsiderNameResponse;
import com.example.housekeeper_android.ui.Network.Get.GetRecordListResponse;
import com.example.housekeeper_android.ui.Network.Post.PostEditOutsiderNameRequest;
import com.example.housekeeper_android.ui.Network.Post.PostEditOutsiderNameResponse;
import com.example.housekeeper_android.ui.activity.DoorActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutsiderDetailRVAdapter extends RecyclerView.Adapter<OutsiderDetailRVAdapter.ViewHolder> {

    ArrayList<OutsiderDetailData> dataList;
    Context ctx;
    Integer idx;

    public OutsiderDetailRVAdapter(Context ctx, ArrayList<OutsiderDetailData> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_outsider_detail_rv_item, viewGroup, false);
        return new OutsiderDetailRVAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Glide.with(ctx).load(dataList.get(position).photo).into(viewHolder.ivOutsiderDetailPhoto);
        idx = dataList.get(position).idx;

        viewHolder.ivOutsiderDetailPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        ctx);
                alertBuilder.setTitle("변경할 외부인 이름을 선택하세요.");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ctx, android.R.layout.simple_list_item_1);

                Call<GetOutsiderNameResponse> getOutsiderNameResponseCall = ApplicationController.getInstance().getNetworkService().getOutsiderNameResponse();
                getOutsiderNameResponseCall.enqueue(new Callback<GetOutsiderNameResponse>() {
                    @Override
                    public void onResponse(Call<GetOutsiderNameResponse> call, Response<GetOutsiderNameResponse> response) {
                        Log.d("RESPONSE_TEST", response.body().result.toString());
                        for (int i = 0; i < response.body().result.size(); i++) {
                            adapter.add(response.body().result.get(i));
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<GetOutsiderNameResponse> call, Throwable t) {
                        Toast.makeText(ctx, "FAIL", Toast.LENGTH_SHORT);
                    }
                });

                // 버튼 생성
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });


                alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        // TODO: 외부인 이동시키기
                        Call<PostEditOutsiderNameResponse> postEditOutsiderNameResponseCall = ApplicationController.getInstance().getNetworkService().postEditOutsiderNameResponse(new PostEditOutsiderNameRequest(idx, adapter.getItem(which).toString()));
                        postEditOutsiderNameResponseCall.enqueue(new Callback<PostEditOutsiderNameResponse>() {
                            @Override
                            public void onResponse(Call<PostEditOutsiderNameResponse> call, Response<PostEditOutsiderNameResponse> response) {
                                Toast.makeText(ctx, "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(which);
                                dataList.remove(which);
                            }

                            @Override
                            public void onFailure(Call<PostEditOutsiderNameResponse> call, Throwable t) {
                                Toast.makeText(ctx, "FAIL.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                alertBuilder.show();

            }
        });

        viewHolder.ivOutsiderDetailPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        ctx);
                alertBuilder.setTitle("삭제하시겠습니까?");
                alertBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        Call<DeleteOutsiderRecordResponse> deleteOutsiderRecordResponseCall = ApplicationController.getInstance().getNetworkService().deleteOutsiderRecordResponse(idx);
                        deleteOutsiderRecordResponseCall.enqueue(new Callback<DeleteOutsiderRecordResponse>() {
                            @Override
                            public void onResponse(Call<DeleteOutsiderRecordResponse> call, Response<DeleteOutsiderRecordResponse> response) {
                                Toast.makeText(ctx, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(which);
                                dataList.remove(which+1);
                            }

                            @Override
                            public void onFailure(Call<DeleteOutsiderRecordResponse> call, Throwable t) {
                                Toast.makeText(ctx, "FAIL.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                alertBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOutsiderDetailPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOutsiderDetailPhoto = (ImageView) itemView.findViewById(R.id.ivOutsiderDetailPhoto);
        }
    }
}
