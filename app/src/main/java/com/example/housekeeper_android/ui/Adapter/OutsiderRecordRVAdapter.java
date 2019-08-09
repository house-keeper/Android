package com.example.housekeeper_android.ui.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Data.DoorRecordData;
import com.example.housekeeper_android.ui.Data.OutsiderRecordData;
import com.example.housekeeper_android.ui.Fragment.OutsiderDetailFragment;

import java.util.ArrayList;

public class OutsiderRecordRVAdapter extends RecyclerView.Adapter<OutsiderRecordRVAdapter.ViewHolder>{

    ArrayList<OutsiderRecordData> dataList;
    Context ctx;

    public OutsiderRecordRVAdapter(Context ctx, ArrayList<OutsiderRecordData> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_outsider_rv_item, viewGroup,false);
        return new OutsiderRecordRVAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Glide.with(ctx).load(dataList.get(position).photo).into(viewHolder.ivOutsiderPhoto);

        viewHolder.tvOutsiderName.setText(dataList.get(position).name);
        viewHolder.tvOutsiderCount.setText("방문횟수: "+dataList.get(position).count.toString());
        viewHolder.llOutsiderRVItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKTEST:","true");
                OutsiderDetailFragment fragment = new OutsiderDetailFragment(); // Fragment 생성
                Bundle bundle = new Bundle();
                bundle.putString("name", dataList.get(position).name);
                fragment.setArguments(bundle);

//                ((AppCompatActivity)ctx).getFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, fragment, "MyFragmentIMGoingTo")
//                        .addToBackStack("MyFragmentIMGoingTo").commit();

                FragmentManager fragmentManager = ((AppCompatActivity)ctx).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction  = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();


            }
        });
    }

    @Override
    public int getItemCount() { return dataList.size(); }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOutsiderPhoto;
        TextView tvOutsiderName;
        TextView tvOutsiderCount;
        LinearLayout llOutsiderRVItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOutsiderPhoto = (ImageView) itemView.findViewById(R.id.ivOutsiderPhoto);
            tvOutsiderName = (TextView) itemView.findViewById(R.id.tvOutsiderName);
            tvOutsiderCount = (TextView) itemView.findViewById(R.id.tvOutsiderCount);
            llOutsiderRVItem = (LinearLayout) itemView.findViewById(R.id.llOutsiderRVItem);
        }
    }
}