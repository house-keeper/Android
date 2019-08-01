package com.example.housekeeper_android.ui.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.housekeeper_android.ui.Network.Delete.DeleteWindowRecordResponse;
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
            public void onResponse(Call<GetWindowRecordResponse> call, final Response<GetWindowRecordResponse> response) {
                Log.d("RESPONSE_TEST",response.body().result.get(0).toString());
                windowRecordRVAdapter = new WindowRecordRVAdapter(response.body().result,rootView.getContext());
                rvWindowRecord.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
                rvWindowRecord.setHasFixedSize(true);
                rvWindowRecord.setAdapter(windowRecordRVAdapter);


                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    Paint p = new Paint();
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("기록을 삭제하시겠습니까?")
                                .setCancelable(false)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Call<DeleteWindowRecordResponse> deleteWindowRecordResponseCall = ApplicationController.getInstance().getNetworkService().deleteWindowRecordResponse(response.body().result.get(viewHolder.getAdapterPosition()).idx);
                                        deleteWindowRecordResponseCall.enqueue(new Callback<DeleteWindowRecordResponse>() {
                                            @Override
                                            public void onResponse(Call<DeleteWindowRecordResponse> call, Response<DeleteWindowRecordResponse> response) {
                                                if(response.isSuccessful()){
                                                    if(response.body().status == 200){
                                                        Toast.makeText(getContext(), "기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DeleteWindowRecordResponse> call, Throwable t) {
                                                Toast.makeText(getContext(), "기록 삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        response.body().result.remove(viewHolder.getAdapterPosition());
                                        rvWindowRecord.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        rvWindowRecord.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        Bitmap icon;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {

                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX/4, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.bar_button_delete);
                                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
                    }
                };

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(rvWindowRecord);
            }

            @Override
            public void onFailure(Call<GetWindowRecordResponse> call, Throwable t) {
                Toast.makeText(getContext(),"FAIL",Toast.LENGTH_SHORT);
            }
        });

        return rootView;
    }

}
