package com.example.housekeeper_android.ui.fragment.dummy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.housekeeper_android.R;


public class RecordDoorFragment extends Fragment {

   // private ListView mListView;


    public RecordDoorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_door, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.record_door_listview);
        RecordDoorAdapter adapter = new RecordDoorAdapter();
        listView.setAdapter(adapter);

        return rootView;


    }

}


