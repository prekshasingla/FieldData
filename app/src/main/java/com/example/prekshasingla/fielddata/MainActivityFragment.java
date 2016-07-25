package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    DBCategoryAdapter dba;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        dba = new DBCategoryAdapter(getActivity());

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                Intent i = new Intent(getActivity(), StoreActivity.class);
                startActivity(i);

                // close this activity
                getActivity().finish();
            }
        }, 3 * 1000);

        return rootView;
    }


    public class FetchCategories extends AsyncTask<Void, Void, Category[]> {
        private final String LOG_TAG = FetchCategories.class.getName();

        private Category[] getMoviesDatafromJson(String moviesJsonString)
                throws JSONException {
            final String OWM_RESULTS = "results";
            final String OWM_ID = "id";
            final String OWM_NAME = "name";
            final String OWM_LABELS = "labels";

            JSONObject moviesJson = new JSONObject(moviesJsonString);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);
            Category[] resultObjects = new Category[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                resultObjects[i] = new Category(movieObject.getInt(OWM_ID), movieObject.getString(OWM_NAME), movieObject.getString(OWM_LABELS));
            }
            return resultObjects;
        }


        @Override
        protected Category[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Category[]type;
            String moviesJsonString = null;

            try {

                URL url = new URL("http://192.168.1.105/fielddata/db_connect.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try { Log.d("Data ", moviesJsonString);
                return getMoviesDatafromJson(moviesJsonString);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Category[] result) {
            if (result != null) {
                dba.removeAll();
                dba.add(result);
                // categories.clear();
                //categories.add(result);
                //dataAdapter.notifyDataSetChanged();
            }
        }
    }
}
