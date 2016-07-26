package com.example.prekshasingla.fielddata;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by prekshasingla on 7/25/2016.
 */
public class SyncTask extends AsyncTask<FieldData,Void,Void> {

    private final String LOG_TAG = SyncTask.class.getName();

     JSONObject putDataToJsonArray(FieldData fieldDatas)throws JSONException {
        final String OWM_IMAGE = "image";
        final String OWM_VIDEO = "video";
        final String OWM_TEXT = "text";
        final String OWM_LATITUDE = "latitude";
        final String OWM_LONGITUDE = "longitude";
        final String OWM_CATEGORY = "category";

        JSONObject json = new JSONObject();

            json.put(OWM_IMAGE,fieldDatas.image);
            json.put(OWM_VIDEO,fieldDatas.video);
            json.put(OWM_TEXT,fieldDatas.text);
            json.put(OWM_LATITUDE,fieldDatas.latitude);
            json.put(OWM_LONGITUDE,fieldDatas.longitude);
            json.put(OWM_CATEGORY,fieldDatas.category);
        return json;

    }

    @Override
    protected Void doInBackground(FieldData... fieldDatas) {

        HttpURLConnection urlConnection = null;
        String data = null;

        final String OWM_RESULTS = "results";

        JSONObject finalJson = new JSONObject();
        JSONArray jsonArray=new JSONArray();

        try {
            //URL url = new URL("http://192.168.1.105/fielddata/db_insert_fielddata.php");
            URL url = new URL("http://192.168.1.34/fielddata/db_insert_fielddata.php");
            //URL url = new URL("http://192.168.1.34/fielddata/db_test.php");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            try {

                for (int i = 0; i < fieldDatas.length; i++) {
                    jsonArray.put(putDataToJsonArray(fieldDatas[i]));
                }
                finalJson.put(OWM_RESULTS,jsonArray);
                data=finalJson.toString();
               // data=jsonArray.toString();

            } catch (Exception e) {
            }

            Log.d("Json:  ", data);

           urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            //urlConnection.setChunkedStreamingMode(0);

            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            //urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //urlConnection.setRequestProperty("Content-Type", "text/plain");
            //urlConnection.setRequestProperty("charset", "utf-8");

            java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(urlConnection.getOutputStream());
            //out.write(data.getBytes("UTF-8"));
            //BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            PrintStream pstream = new PrintStream(out);
            pstream.print(data);
            pstream.close();
            //bufferedWriter.write(data);
            //bufferedWriter.close();

            //urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            //urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");


            //urlConnection.connect();


            /*
            try {
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("Connection ", "established");
                }

            else{

            }
        }
        catch (Exception e){}
*/

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

}
