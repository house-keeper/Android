package com.example.housekeeper_android.ui.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Data.DoorRecordData;
import com.example.housekeeper_android.ui.Data.RecordData;

import java.util.ArrayList;

public class DoorRecordRVAdapter extends RecyclerView.Adapter<DoorRecordRVAdapter.ViewHolder> {

   ArrayList<DoorRecordData> dataList;
   Context ctx;

   public DoorRecordRVAdapter(Context ctx, ArrayList<DoorRecordData> dataList) {
      this.dataList = dataList;
      this.ctx = ctx;
   }


   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_door_rv_item, viewGroup,false);
      return new DoorRecordRVAdapter.ViewHolder(itemView);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//      Log.d("IMGTEST:: ",dataList.get(position).photo.toString());

      if(dataList.get(position).status == 1) viewHolder.tvDoorRecordStatus.setText("현관문이 열렸습니다.");
      else viewHolder.tvDoorRecordStatus.setText("현관문이 닫혔습니다.");

      viewHolder.tvDoorRecordTime.setText(dataList.get(position).time);
   }

   @Override
   public int getItemCount() {
      return dataList.size();
   }

   static class ViewHolder extends RecyclerView.ViewHolder{
     TextView tvDoorRecordStatus;
     TextView tvDoorRecordTime;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);
         tvDoorRecordStatus = (TextView) itemView.findViewById(R.id.tvDoorRecordStatus);
         tvDoorRecordTime = (TextView) itemView.findViewById(R.id.tvDoorRecordTime);
      }
   }
}