package com.example.prekshasingla.fielddata;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

//import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

class ImageAdapter extends ArrayAdapter<FieldData>
{

    public ImageAdapter(Activity context, ArrayList<FieldData> fieldDataList) {
        super(context, 0, fieldDataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldData fieldData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap photo=null;
        if(fieldData.image!=null) {
            String img = fieldData.image;
            if (img.getBytes() != null) {
                byte[] imageAsBytes = Base64.decode(img.getBytes(), Base64.DEFAULT);
                photo = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                posterView.setImageBitmap(photo);
            }
        }
        return convertView;
    }


}
