package com.example.housekeeper_android.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.housekeeper_android.R;

public class SettingActivity extends AppCompatActivity {

    RelativeLayout rlGoRecordActivity, rlGoAddressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rlGoRecordActivity = (RelativeLayout)findViewById(R.id.rlGoRecordAcitivity);
        rlGoAddressActivity = (RelativeLayout)findViewById(R.id.rlGoAddressAcitivity);

        rlGoRecordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, RecordvoiceActivity.class));
            }
        });
        rlGoAddressActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AddressActivity.class));
            }
        });
    }
}
