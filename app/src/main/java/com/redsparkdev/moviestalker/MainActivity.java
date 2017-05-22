package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.redsparkdev.moviestalker.utilities.FetchMovieData;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;

public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterOnClickHandler{

    private static final int SEARCH_LOADER = 1;



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
        int spanCount = 2;//number of colums
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter(this);

        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onClick(MovieInfo movieInfoObject) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra("MovieInfoObject", movieInfoObject);
        startActivity(intent);

    }
    private void  loadMovieData(String sortBy){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtil.SortBy.KEY, sortBy);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieInfo[]> movieSearch = loaderManager.getLoader(SEARCH_LOADER);
        if (movieSearch == null) {
            loaderManager.initLoader(SEARCH_LOADER, queryBundle, new FetchMovieData(this));
        } else {
        loaderManager.restartLoader(SEARCH_LOADER, queryBundle, new FetchMovieData(this));
    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //handles the drop down menu aka spinner
        MenuItem item = menu.findItem(R.id.spinner_sort_by);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Listens for dropdown menu selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals(getString(R.string.sortBy_most_popular))){
                    loadMovieData(NetworkUtil.SortBy.POPULAR);
                }else if(selectedItem.equals(getString(R.string.sortBy_highest_rated))){
                    loadMovieData(NetworkUtil.SortBy.TOP_RATED);
                }


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return true;
    }
}
