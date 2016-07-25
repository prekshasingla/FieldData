package com.example.prekshasingla.fielddata;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class StoreDetailActivityFragment extends Fragment {


    public static final String TAG = StoreDetailActivityFragment.class.getSimpleName();

    private static final int CAMERA_REQUEST = 1888;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private ImageView imageView;
    List list;
    LinearLayout linearLayout,labelLayout;
    String loc= "No Loc";
    EditText ed;
    TextView tv;
    ArrayList<String> textList;
    Button save,btn,videoBtn;
    int j;
    View rootView;
    ArrayList<String> labels;
    //byte[] image=null;
    String text=null,category=null,latitude=null,longitude=null,image=null,video=null;
    DBAdapter dba;
    File file;
    Uri videoUri;
    LocationManager locationManager;
GPS gps;

    public StoreDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dba= new DBAdapter(getActivity());
        Intent i = getActivity().getIntent();
        text= i.getStringExtra("text");
        category=i.getStringExtra("category");
        textList=new ArrayList<String>();
        rootView = inflater.inflate(R.layout.fragment_store_detail, container, false);

        gps=new GPS();

        linearLayout= (LinearLayout)rootView.findViewById(R.id.linearlayout);
        labelLayout= (LinearLayout)rootView.findViewById(R.id.labellayout);

        imageView = (ImageView)rootView.findViewById(R.id.ivImage);
        btn= (Button)rootView.findViewById(R.id.btnSelectPhoto);
        save= (Button)rootView.findViewById(R.id.save_button);
        btn.performClick();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                video=null;
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        videoBtn=(Button)rootView.findViewById(R.id.btnSelectVideo);
        videoBtn.performClick();
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image=null;
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
        });


        labels=showLabel(text);
        View []convertView=new View[labels.size()];
        for(j = 0; j <labels.size(); j++) {
            convertView[j]=LayoutInflater.from(getContext()).inflate(R.layout.label_layout, labelLayout , false);
            convertView[j].setId(j);
            tv=(TextView)convertView[j].findViewById(R.id.label);
            tv.setText(labels.get(j));
            ed =(EditText)convertView[j].findViewById(R.id.edit_text);
            labelLayout.addView(convertView[j]);
        }

        save.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        for(j = 0; j <labels.size(); j++) {
                            View convertView1 = (View) rootView.findViewById(j);
                            ed = (EditText) convertView1.findViewById(R.id.edit_text);
                            textList.add(ed.getText().toString());
                            TextView tv1 = new TextView(getActivity());
                            tv1.setText(ed.getText().toString());
                            linearLayout.addView(tv1);
//                                Log.v("EditText", ed.getText().toString());
                        }
                        text=combineLabels(textList);

                        if(checkUserPermission()) {
                            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
                            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = "" + location.getLatitude();
                                longitude = "" + location.getLongitude();
                            }
                            else
                            {
                               // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gps);
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                latitude = "" + location.getLatitude();
                                longitude = "" + location.getLongitude();
                            }
                        }

                        if(latitude!=null && longitude!=null && category!=null){
                            try {
                                dba.open();
                            } catch (SQLException e) {
                                Log.e("SqlException", e.toString());
                            }
                            dba.updateFavourite(image,video,latitude,longitude,text,category);
                            dba.close();

                            Toast.makeText(getActivity(), "Saving", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Location not available", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image = dba.bitmapToBase64(photo);

            imageView.setImageBitmap(photo);
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            videoUri = data.getData();
            file=new File(getRealPathFromUri(videoUri));
            //Log.i("Strng", file.toString());

            dba.fileToBase64(file);
            //mVideoView.setVideoURI(videoUri);
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(tempUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public ArrayList<String> showLabel(String text)
    {   String[] items = text.split(",");
        ArrayList<String> list=new ArrayList<String>(Arrays.asList(items));
        return list;
    }

    public String combineLabels(ArrayList<String> text)
    {
        String a=text.get(0);
        for(int i=1;i<text.size();i++)
        {
                a=a.concat(","+text.get(i));
        }
        return a;
    }

    public boolean checkUserPermission()
    {
        int statusintfine = getActivity().getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getActivity().getPackageName());
        int statusintcoarse = getActivity().getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,getActivity().getPackageName());
        boolean flag=false;

        if (statusintfine != PackageManager.PERMISSION_GRANTED && statusintcoarse != PackageManager.PERMISSION_GRANTED) {
            flag = false;
        }
        else { flag=true; }
        return flag;
    }

    class GPS implements LocationListener {


        public double latitude;
        public double longitude;
        public LocationManager locationManager;
        public Criteria criteria;
        public String bestProvider;


        public boolean checkUserPermission()
        {
            int statusintfine = getActivity().getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getActivity().getPackageName());
            int statusintcoarse = getActivity().getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,getActivity().getPackageName());
            boolean flag=false;

            if (statusintfine != PackageManager.PERMISSION_GRANTED && statusintcoarse != PackageManager.PERMISSION_GRANTED) {
                flag = false;
            }
            else { flag=true; }
            return flag;
        }



        @Override
        public void onLocationChanged(Location location) {
            //Hey, a non null location! Sweet!

            if(checkUserPermission()) {
                //remove location callback:
                locationManager.removeUpdates(this);

                //open the map:
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(getActivity(), "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
