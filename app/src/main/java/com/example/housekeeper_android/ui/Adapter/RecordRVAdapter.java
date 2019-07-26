package com.example.housekeeper_android.ui.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Data.RecordData;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Delete.DeleteRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetRecordListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordRVAdapter extends RecyclerView.Adapter<RecordRVAdapter.ViewHolder> {

    ArrayList<RecordData> dataList;
    Context ctx;

    public class IsPlaying{
        boolean isPlaying;
        public IsPlaying(boolean isPlaying) { this.isPlaying = isPlaying; }
        public boolean isPlaying() { return isPlaying; }
        public void setPlaying(boolean playing) { isPlaying = playing; }
    }
    public RecordRVAdapter(Context ctx, ArrayList<RecordData> dataList) {
        this.ctx = ctx;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_rv_item, viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final MediaPlayer mPlayer = new MediaPlayer();
        final String mPath=dataList.get(position).file;
        final IsPlaying isPlaying = new IsPlaying(false);

        viewHolder.recordFileName.setText(dataList.get(position).fileName);
        viewHolder.btnRecordListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPlaying.isPlaying() == false) {
                    try {
                        mPlayer.reset();
                        mPlayer.setDataSource(mPath);
                        mPlayer.prepare();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();

                    isPlaying.setPlaying(true);
                    viewHolder.btnRecordListen.setText("멈춤");
                }
                else {
                    mPlayer.stop();

                    isPlaying.setPlaying(false);
                    viewHolder.btnRecordListen.setText("듣기");
                }
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("FINISH!",String.valueOf(position));
                isPlaying.setPlaying(false);
                viewHolder.btnRecordListen.setText("듣기");
            }
        });

        viewHolder.btnRecordDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DeleteRecordResponse> deleteRecordResponseCall = ApplicationController.getInstance().getNetworkService().deleteRecord(dataList.get(position).idx);
                deleteRecordResponseCall.enqueue(new Callback<DeleteRecordResponse>() {
                    @Override
                    public void onResponse(Call<DeleteRecordResponse> call, Response<DeleteRecordResponse> response) {
                        Log.d("RESPONSE_TEST",String.valueOf(response.body()));
                       // Toast.makeText(ctx,"삭제가 완료되었습니다.",Toast.LENGTH_SHORT);
                        Log.d("TESTTEST","삭제완료");
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<DeleteRecordResponse> call, Throwable t) {
                        Toast.makeText(ctx,"Delete FAIL.",Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView recordFileName;
        Button btnRecordListen;
        Button btnRecordDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recordFileName = (TextView)itemView.findViewById(R.id.tvRecordFileName);
            btnRecordListen = (Button)itemView.findViewById(R.id.btnRecordListen);
            btnRecordDelete = (Button)itemView.findViewById(R.id.btnRecordDelete);
        }
    }
}
