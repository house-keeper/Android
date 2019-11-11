package com.example.housekeeper_android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.DoorRecordRVAdapter;
import com.example.housekeeper_android.ui.Fragment.OutsiderDetailFragment;
import com.example.housekeeper_android.ui.Fragment.RecordDoorFragment;
import com.example.housekeeper_android.ui.Fragment.RecordOutsiderFragment;
import com.example.housekeeper_android.ui.Fragment.RecordWindowFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar record_toolbar;

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;
    private Button doortab, windowtab, outsidertab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //툴바 관련
        record_toolbar = (Toolbar) findViewById(R.id.record_toolbar);
        setSupportActionBar(record_toolbar);
        getSupportActionBar().setTitle("");
        // 위젯에 대한 참조
        doortab = (Button)findViewById(R.id.doorTab); doortab.setSelected(true);
        windowtab = (Button)findViewById(R.id.windowTab); windowtab.setSelected(false);
        outsidertab = (Button)findViewById(R.id.outsiderTab); outsidertab.setSelected(false);
        // 탭 버튼에 대한 리스너 연결
        doortab.setOnClickListener(this);
        windowtab.setOnClickListener(this);
        outsidertab.setOnClickListener(this);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.doorTab :
                v.setSelected(true);
                windowtab.setSelected(false);
                outsidertab.setSelected(false);

                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.windowTab :
                v.setSelected(true);
                doortab.setSelected(false);
                outsidertab.setSelected(false);

                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;

            case R.id.outsiderTab :
                v.setSelected(true);
                doortab.setSelected(false);
                windowtab.setSelected(false);

                // '버튼3' 클릭 시 '프래그먼트3' 호출
                callFragment(FRAGMENT3);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                RecordDoorFragment fragment1 = new RecordDoorFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                RecordWindowFragment fragment2 = new RecordWindowFragment();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;

            case 3:
                // '프래그먼트3' 호출
                RecordOutsiderFragment fragment3 = new RecordOutsiderFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;

            case 4:
                // '프래그먼트4' 호출
                OutsiderDetailFragment fragment4 = new OutsiderDetailFragment();
                transaction.replace(R.id.fragment_container, fragment4);
                transaction.commit();
                break;

        }

    }


}
