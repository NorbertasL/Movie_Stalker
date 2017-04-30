package com.redsparkdev.moviestalker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.redsparkdev.moviestalker.utilities.FetchMovieData;
import com.redsparkdev.moviestalker.utilities.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;

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

        //Will be using a GridLayoutManager for displaying the images
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
        Toast.makeText(this, movieId, Toast.LENGTH_LONG).show();

    }
    private void  loadMovieData(){

        new FetchMovieData(this).execute(NetworkUtil.SortBy.POPULAR);


    }

    //Methods to manage views
    public void showLoadingIndicator(){
        loadingIndicator.setVisibility(View.VISIBLE);
    }
    public void showErrorMessage(){
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);

    }
    public void showMovieData(){
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    public void setMovieData(MovieInfo[] movieData){
        myAdapter.setMovieData(movieData);
    }
}
