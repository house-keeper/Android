package com.example.housekeeper_android.ui.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Data.DoorRecordData;
import com.example.housekeeper_android.ui.Data.WindowRecordData;

import java.util.ArrayList;

public class WindowRecordRVAdapter extends RecyclerView.Adapter<WindowRecordRVAdapter.ViewHolder> {

    ArrayList<WindowRecordData> dataList;
    Context ctx;

    public WindowRecordRVAdapter(ArrayList<WindowRecordData> dataList, Context ctx) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_window_rv_item, viewGroup,false);
        return new WindowRecordRVAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(dataList.get(position).window_status == 1) viewHolder.tvWindowRecordStatus.setText("창문이 열렸습니다.");
        else viewHolder.tvWindowRecordStatus.setText("창문이 닫혔습니다.");

        viewHolder.tvWindowRecordTime.setText(dataList.get(position).window_time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvWindowRecordStatus;
        TextView tvWindowRecordTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWindowRecordStatus = (TextView) itemView.findViewById(R.id.tvWindowRecordStatus);
            tvWindowRecordTime = (TextView) itemView.findViewById(R.id.tvWindowRecordTime);
        }
    }
}
