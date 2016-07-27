package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;

public class StoreActivity extends AppCompatActivity {

    DBAdapter dba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dba=new DBAdapter(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;    }

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

            if(CheckNetwork.isInternetAvailable(this)) //returns true if internet available
            {

                try {
                    dba.open();
                } catch (SQLException e) {
                    Log.e("SqlException", e.toString());
                }
                FieldData[] fieldDatas = dba.show();
                dba.close();
                new SyncTask().execute(fieldDatas);



                Toast.makeText(this, "Synchronised", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
