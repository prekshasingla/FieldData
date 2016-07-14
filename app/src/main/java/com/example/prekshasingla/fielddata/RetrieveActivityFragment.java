package com.example.prekshasingla.fielddata;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RetrieveActivityFragment extends Fragment {
    DBAdapter dba;
    private ImageAdapter moviesAdapter;

    private ArrayList<FieldData> currentMovieList = new ArrayList<>();

    public RetrieveActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dba = new DBAdapter(getActivity());
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        new FetchFavoriteMoviesTask().execute();
        //Initializes our custom adapter for the Gridview with the current Movie ArrayList data and fetches id's to identify Gridview
        moviesAdapter = new ImageAdapter(getActivity(), currentMovieList);
        View rootView = inflater.inflate(R.layout.fragment_retrieve, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);

        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FieldData currentMovie = moviesAdapter.getItem(position);
                Intent i = new Intent(getActivity(),RetrieveDetailActivity.class);
                i.putExtra("image",currentMovie.image);
                i.putExtra("latitude",currentMovie.latitude);
                i.putExtra("longitude",currentMovie.longitude);
                i.putExtra("text",currentMovie.text);
                i.putExtra("category",currentMovie.category);
                i.putExtra("id",currentMovie.id);
                startActivity(i);
            }
        });
        return rootView; }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, FieldData[]> {

        @Override
        protected FieldData[] doInBackground(Void... params) {
            try {
                dba.open();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return dba.showFavourite();
        }

        @Override
        protected void onPostExecute(FieldData[] result) {
            if (result != null) {
                moviesAdapter.clear();
                moviesAdapter.addAll(result);
                moviesAdapter.notifyDataSetChanged();
            }
        }
    }
}
