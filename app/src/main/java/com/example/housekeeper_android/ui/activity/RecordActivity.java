package com.example.housekeeper_android.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.fragment.dummy.RecordDoorFragment;
import com.example.housekeeper_android.ui.fragment.dummy.RecordWindowFragment;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar record_toolbar;

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private Button doortab, windowtab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //툴바 관련
        record_toolbar = (Toolbar) findViewById(R.id.record_toolbar);
        setSupportActionBar(record_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.bar_button_return);

        // 위젯에 대한 참조
        doortab = (Button)findViewById(R.id.doorTab);
        windowtab = (Button)findViewById(R.id.windowTab);
        // 탭 버튼에 대한 리스너 연결
        doortab.setOnClickListener(this);
        windowtab.setOnClickListener(this);

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);
    }


    //툴바에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.record_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.doorTab :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.windowTab :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
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
        }

    }

}
