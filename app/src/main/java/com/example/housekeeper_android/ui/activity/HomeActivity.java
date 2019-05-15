package com.example.housekeeper_android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.housekeeper_android.R;

public class HomeActivity extends AppCompatActivity {

    Button goto_door, goto_window, goto_record, goto_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        goto_door=(Button)findViewById(R.id.home_goto_door);
        goto_window=(Button)findViewById(R.id.home_goto_window);
        goto_record=(Button)findViewById(R.id.home_goto_record);
        goto_setting=(Button)findViewById(R.id.home_goto_setting);

        goto_door.setOnClickListener(listener);
        goto_window.setOnClickListener(listener);
        goto_record.setOnClickListener(listener);
        goto_setting.setOnClickListener(listener);

    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.home_goto_door:
                    Intent intent = new Intent(HomeActivity.this, DoorActivity.class);
                    startActivity(intent);
                    break;
                case R.id.home_goto_window:
                    Intent intent2 = new Intent(HomeActivity.this, WindowActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.home_goto_record:
                    Intent intent3 = new Intent(HomeActivity.this, RecordActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.home_goto_setting:
                    Intent intent4 = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(intent4);
                    break;
            }
        }
    };

}
