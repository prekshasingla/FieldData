package com.example.prekshasingla.fielddata;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import javax.net.ssl.HttpsURLConnection;

public class StoreActivity extends AppCompatActivity {

    DBAdapter dba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dba = new DBAdapter(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_review) {
            Intent i = new Intent(this, RetrieveActivity.class);
            startActivity(i);

            return true;
        }

        if (id == R.id.action_sync) {

            if (CheckNetwork.isInternetAvailable(this)) //returns true if internet available
            {

                try {
                    dba.open();
                } catch (SQLException e) {
                    Log.e("SqlException", e.toString());
                }
                FieldData[] fieldDatas = dba.show();
                dba.close();

                new SyncTask().execute(fieldDatas);
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        super.onOptionsItemSelected(item);
        return false;
    }

    public class SyncTask extends AsyncTask<FieldData, Void, Void> {

        private final String LOG_TAG = SyncTask.class.getName();

        @Override
        protected Void doInBackground(FieldData... fieldDatas) {

            HttpURLConnection urlConnection = null;
            URL url[] = new URL[fieldDatas.length];
            try {

                for (int i = 0; i < fieldDatas.length; i++) {

                    if (fieldDatas[i].video != null) {
                        File f = new File(fieldDatas[i].video);
                        fieldDatas[i].video = Utils.fileToBase64(f);
                        //Log.d("Video", fieldDatas[i].video);
                    }


                    //url[i] = new URL("http://pieronline.rmsi.com/Rapti_publish/RestServiceImpl.svc/InsertData?image='" + fieldDatas[i].image + "'&video='" + fieldDatas[i].video + "'&latitude=" + fieldDatas[i].latitude + "&longitude=" + fieldDatas[i].longitude + "&label=" + fieldDatas[i].text + "&category=" + fieldDatas[i].category);

                    //Log.d("image",fieldDatas[i].image);
                    //Log.d("video",fieldDatas[i].video);
                    //Log.d("Lat",fieldDatas[i].latitude);
                    //urlConnection = (HttpURLConnection) url[i].openConnection();
                    //urlConnection.setReadTimeout(25000);
                    //urlConnection.setConnectTimeout(25000);
                    //urlConnection.setRequestMethod("GET");
                    //Log.d("URL", url[i].toString());
                    //urlConnection.connect();
                    try {
                        dba.open();
                    } catch (SQLException e) {
                        Log.e("SqlException", e.toString());
                    }
                    dba.removeFavourite(fieldDatas[i].id);
                    dba.close();


                    try {
                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            Log.d("Connection ", "established");
                        } else {

                        }
                    } catch (Exception e) {
                    }

                }
            } catch (Exception e) {
                //Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                //urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplication(), "Synchronised", Toast.LENGTH_LONG).show();
        }
    }

}
