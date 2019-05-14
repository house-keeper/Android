package com.example.housekeeper_android.ui.fragment.dummy;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.housekeeper_android.R;

import java.util.ArrayList;

public class RecordDoorAdapter extends BaseAdapter {

   String[] arr2= {"현관문이 닫혔습니다.", "현관문이 열렸습니다.", "외부인을 감지했습니다." };
   String[] arr={"2019-04-10 18:31:04","2019-04-10 18:30:55","2019-04-10 17:11:28"};

   @Override
    public int getCount(){
       return  arr.length;
   }

   @Override
    public Object getItem(int position){
       return arr[position];
   }

   @Override
    public long getItemId(int position){
       return position;
   }

   @Override
    public View getView(int position, View convertView, ViewGroup parent){
       RecordDooritem listItem = new RecordDooritem(parent.getContext());
       listItem.setFirstItem(arr2[position]);
       listItem.setSecondItem(arr[position]);
       return listItem;
   }
}