package com.example.housekeeper_android.ui.etc;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrernceController {
    private static final String ADDRESS = "address";

    public static void setAddress(Context context, String address){
        SharedPreferences pref = context.getSharedPreferences(ADDRESS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ADDRESS, address);
        editor.commit();
    }
    public static String getAddress(Context context){
        SharedPreferences pref = context.getSharedPreferences(ADDRESS, context.MODE_PRIVATE);
        String address = pref.getString(ADDRESS,"");
        return address;
    }
}
