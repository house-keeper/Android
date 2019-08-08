package com.example.housekeeper_android.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.etc.SharedPrefrernceController;

public class AddressActivity extends AppCompatActivity {

    Button btnSaveAddress;
    EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        btnSaveAddress = (Button)findViewById(R.id.btnSaveAddress);
        etAddress = (EditText)findViewById(R.id.etAddress);

        etAddress.setText(SharedPrefrernceController.getAddress(this));

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddress.getText().length() == 0){
                    Toast.makeText(AddressActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPrefrernceController.setAddress(AddressActivity.this,etAddress.getText().toString());
                    Toast.makeText(AddressActivity.this, "주소가 저장되었습니다. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
