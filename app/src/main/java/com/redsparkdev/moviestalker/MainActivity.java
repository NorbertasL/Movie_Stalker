package com.redsparkdev.moviestalker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterOnClickHandler{
    private RecyclerView recyclerView;
    private TextView errorMessageDisplay;
    private ProgressBar loadingIndicator;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Finding an binding ids
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);
        errorMessageDisplay = (TextView)findViewById(R.id.error_message_display);
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);

        //Will be using a GridLayout for displaying the images
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter(this);

        recyclerView.setAdapter(myAdapter);

        loadMovieData();
    }

    @Override
    public void onClick(String movieId) {

    }
    private void  loadMovieData(){
        String[] temp = {"1", "2,", "3"};
        myAdapter.setMovieData(temp);


    }
}
