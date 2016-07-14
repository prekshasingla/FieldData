package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button storebtn= (Button)rootView.findViewById(R.id.Store_btn);
        //storebtn.performClick();
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Store", "msg");
                Intent i = new Intent(getActivity(), StoreActivity.class);
                startActivity(i);


            }
        });
        Button retrievebtn= (Button)rootView.findViewById(R.id.Retrieve_btn);
        //retrievebtn.performClick();
        retrievebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RetrieveActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Retrieve", Toast.LENGTH_SHORT).show();
               }
        });

        return rootView;
    }
}
