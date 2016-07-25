package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class StoreActivityFragment extends Fragment {

    public static final String TAG = StoreActivityFragment.class.getSimpleName();

    ArrayAdapter<String> dataAdapter;
    List<String> categories;

    public StoreActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Intent i = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        // Spinner element
        MySpinner spinner = (MySpinner) rootView.findViewById(R.id.spinner);

        // Spinner Drop down elements
        categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Category1");
        categories.add("Category2");
        categories.add("Category3");
        categories.add("Category4");
        categories.add("Category5");
        categories.add("Category6");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories){ @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            View v = null;

            if (position == 0) {
                TextView tv = new TextView(getContext());
                tv.setHeight(0);
                tv.setVisibility(View.GONE);
                v = tv;
            }
            else {

                v = super.getDropDownView(position, null, parent);
            }

            parent.setVerticalScrollBarEnabled(false);
            return v;
        }
        };


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        FetchCategories fetchCategories=new FetchCategories();
        fetchCategories.execute();

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String category= parent.getItemAtPosition(position).toString();
                String text=null;
                switch(category)
                {
                    case "Category1": text="label1.1,label1.2";
                        break;
                    case "Category2": text="label2.1,label2.2";
                        break;

                    case "Category3": text="label3.1,label3.2";
                        break;

                    case "Category4": text="label4.1,label4.2";
                        break;

                    case "Category5": text="label5.1,label5.2";
                        break;

                    case "Category6": text="label6.1,label6.2";
                        break;
                    default:
                        break;
                }

                if(text!=null) {
                    Intent i = new Intent(getActivity(), StoreDetailActivity.class);
                    i.putExtra("category",category);
                    i.putExtra("text",text);
                    startActivity(i);

                    Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;

    }

    public class FetchCategories extends AsyncTask<Void, Void, String> {
        private final String LOG_TAG = FetchCategories.class.getName();

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

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
                return moviesJsonString;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
               // categories.clear();
                //categories.add(result);
                //dataAdapter.notifyDataSetChanged();
            }
        }
    }
}
