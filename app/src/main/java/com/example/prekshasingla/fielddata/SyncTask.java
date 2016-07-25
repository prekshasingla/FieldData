package com.example.prekshasingla.fielddata;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by prekshasingla on 7/25/2016.
 */
public class SyncTask extends AsyncTask<FieldData,Void,Void> {

    private final String LOG_TAG = SyncTask.class.getName();

     String putDataToJson(FieldData[] fieldDatas)throws JSONException {
        final String OWM_RESULTS = "results";
        final String OWM_IMAGE = "image";
        final String OWM_VIDEO = "video";
        final String OWM_TEXT = "text";
        final String OWM_LATITUDE = "latitude";
        final String OWM_LONGITUDE = "longitude";
        final String OWM_CATEGORY = "category";

         JSONObject finalJson = new JSONObject();
         JSONObject json = new JSONObject();
         JSONArray jsonArray=new JSONArray();

         for (int i = 0; i < fieldDatas.length; i++) {

             json.put(OWM_IMAGE,fieldDatas[i].image);
             json.put(OWM_VIDEO,fieldDatas[i].video);
             json.put(OWM_TEXT,fieldDatas[i].text);
             json.put(OWM_LATITUDE,fieldDatas[i].latitude);
             json.put(OWM_LONGITUDE,fieldDatas[i].longitude);
             json.put(OWM_CATEGORY,fieldDatas[i].category);
             jsonArray.put(json);
         }
         finalJson.put(OWM_RESULTS,jsonArray);
         return finalJson.toString();

    }

    @Override
    protected Void doInBackground(FieldData... fieldDatas) {

        HttpURLConnection urlConnection = null;
        String data = null;
        try {

            URL url = new URL("http://192.168.1.105/fielddata/db_insert_fielddata.php?");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            try {
                data = putDataToJson(fieldDatas);
            } catch (Exception e){}

            //Log.d("Json:  ",data);



            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();


             //urlConnection.setFixedLengthStreamingMode(data.getBytes().length);

            //make some HTTP header nicety
            //urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            urlConnection.connect();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
        }
        return null;
    }
}
