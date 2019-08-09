package com.example.housekeeper_android.ui.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.DoorRecordRVAdapter;
import com.example.housekeeper_android.ui.Adapter.RecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Delete.DeleteDoorRecordResponse;
import com.example.housekeeper_android.ui.Network.Delete.DeleteWindowRecordResponse;
import com.example.housekeeper_android.ui.Network.Get.GetDoorRecordResponse;
import com.example.housekeeper_android.ui.activity.RecordActivity;

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
        setHasOptionsMenu(true);

        Call<GetDoorRecordResponse> getDoorRecordResponseCall = ApplicationController.getInstance().getNetworkService().getDoorRecordResponse();
        getDoorRecordResponseCall.enqueue(new Callback<GetDoorRecordResponse>() {
            @Override
            public void onResponse(Call<GetDoorRecordResponse> call, final Response<GetDoorRecordResponse> response) {
                Log.d("RESPONSE_TEST",response.body().result.get(0).toString());
                doorRecordRVAdapter = new DoorRecordRVAdapter(rootView.getContext(),response.body().result);
                rvDoorRecord.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
                rvDoorRecord.setHasFixedSize(true);
                rvDoorRecord.setAdapter(doorRecordRVAdapter);

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

                                        Call<DeleteDoorRecordResponse> deleteDoorRecordResponseCall = ApplicationController.getInstance().getNetworkService().deleteDoorRecordResponse(response.body().result.get(viewHolder.getAdapterPosition()).idx);
                                        deleteDoorRecordResponseCall.enqueue(new Callback<DeleteDoorRecordResponse>() {
                                            @Override
                                            public void onResponse(Call<DeleteDoorRecordResponse> call, Response<DeleteDoorRecordResponse> response) {
                                                if(response.isSuccessful()){
                                                    if(response.body().status == 200){
                                                        Toast.makeText(getContext(), "기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DeleteDoorRecordResponse> call, Throwable t) {
                                                Toast.makeText(getContext(), "기록 삭제 실패.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        response.body().result.remove(viewHolder.getAdapterPosition());
                                        rvDoorRecord.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        rvDoorRecord.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
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
                itemTouchHelper.attachToRecyclerView(rvDoorRecord);
            }

            @Override
            public void onFailure(Call<GetDoorRecordResponse> call, Throwable t) {
                Toast.makeText(getContext(),"FAIL",Toast.LENGTH_SHORT);
            }
        });


        return rootView;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.record_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.record_menu_delete:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        getContext());
                alertBuilder.setTitle("삭제하려면 기록을 스와이프 하세요. ");
                alertBuilder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}


