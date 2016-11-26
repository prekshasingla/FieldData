package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;

/**
 * A placeholder fragment containing a simple view.
 */
public class RetrieveDetailActivityFragment extends Fragment {


    public static final String TAG = RetrieveDetailActivityFragment.class.getSimpleName();
    private DBAdapter dba;

    public RetrieveDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dba= new DBAdapter(getActivity());

        final String id;
        final String image;
        final String latitude;
        final String longitude;
        final String category;
        final String text;
        final String video;


        Intent i = getActivity().getIntent();
        image = i.getStringExtra("image");
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");
        category = i.getStringExtra("category");
        text = i.getStringExtra("text");
        id = i.getStringExtra("id");
        video=i.getStringExtra("video");

        View rootView = inflater.inflate(R.layout.fragment_retrieve_detail, container, false);

        TextView latitude_1 = (TextView) rootView.findViewById(R.id.latitude);
        ImageView image_1 = (ImageView) rootView.findViewById(R.id.imageView);
        //VideoView video_1=(VideoView) rootView.findViewById(R.id.videoView);
        TextView longitude_1 = (TextView) rootView.findViewById(R.id.longitude);
        TextView category_1 = (TextView) rootView.findViewById(R.id.category);
        TextView text_1 = (TextView) rootView.findViewById(R.id.labels_list);


        CheckBox star=(CheckBox)rootView.findViewById(R.id.star);
        //star.setChecked(true);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        dba.open();
                    } catch (SQLException e) {
                        Log.e("SqlException", e.toString());
                    }
                    dba.removeFavourite(id);

                //dba.close();
                    Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                }

        });


        if(image!=null) {
            Bitmap photo = Utils.base64ToBitmap(image);
            image_1.setImageBitmap(photo);
            image_1.setVisibility(View.VISIBLE);
            //video_1.setVisibility(View.GONE);
        }
            if(video!=null)
            {
                File f=new File(video);
                String data=Utils.fileToBase64(f);
                Log.d("data video",data);
                Log.d("data","not available");
                Uri uri=Uri.fromFile(f);
                //video_1.setVideoURI(uri);
                //video_1.setVisibility(View.VISIBLE);
                //image_1.setVisibility(View.GONE);
            }

        latitude_1.setText("Latitude " +latitude);
        longitude_1.setText("Longitude " + longitude);
        category_1.setText("Category " + category);
        text_1.setText("Text "+text);

        return rootView;
    }
}
