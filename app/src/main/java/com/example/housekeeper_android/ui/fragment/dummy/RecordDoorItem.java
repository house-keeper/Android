package com.example.housekeeper_android.ui.fragment.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.housekeeper_android.R;

class RecordDooritem extends LinearLayout {
    TextView door_status;
    TextView door_date;

    public RecordDooritem(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myInflater.inflate(R.layout.record_door_list_item, this, true);

        door_status =(TextView) findViewById(R.id.record_door_status);
        door_date =(TextView) findViewById(R.id.record_door_date);
    }

    public void setSecondItem(String item) {door_status.setText(item); }
    public void setFirstItem(String item) {door_date.setText(item); }



}
