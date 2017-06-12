package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.redsparkdev.moviestalker.storageObjects.Constants;
import com.redsparkdev.moviestalker.utilities.adapters.MainActivityAdapter;
import com.redsparkdev.moviestalker.utilities.loaders.network.FetchMovieData;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements MainActivityAdapter.MyAdapterOnClickHandler{

    private final static String TAG = MainActivity.class.getSimpleName();


    private final static String SORT_ORDER_KEY = "sort";
    private final static String LIST_KEY = "list_key";
    private final static String POS_KEY = "pos_key";


    private MovieInfo [] movieData;
    private String lastSortOrder = "";





    private RecyclerView recyclerView;
    private TextView errorMessageDisplay;
    private ProgressBar loadingIndicator;
    private MainActivityAdapter mainActivityAdapter;
    private GridLayoutManager layoutManager;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //Finding an binding ids
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);
        errorMessageDisplay = (TextView)findViewById(R.id.error_message_display);
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);

        int spanCount = 2;//number of columns
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mainActivityAdapter = new MainActivityAdapter(this);

        recyclerView.setAdapter(mainActivityAdapter);
        if(savedInstanceState != null){
            movieData = (MovieInfo[])savedInstanceState.getParcelableArray(LIST_KEY);
            lastSortOrder = savedInstanceState.getString(SORT_ORDER_KEY);
            setMovieData(movieData);
            layoutManager.scrollToPosition(savedInstanceState.getInt(POS_KEY));


        }else{
                loadMovieData(Constants.SortOrder.POPULAR);

        }
    }


    @Override
    public void onClick(MovieInfo movieInfoObject) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

        //Pass in the movieObject class that has all the data the movie
        intent.putExtra(Constants.ExtraData.OBJECT,(Serializable) movieInfoObject);

        //Launch the detail activity to display more details about the movie
        startActivity(intent);

    }

    //Load movie data based on what sorting order the user has selected
    private void  loadMovieData(String sortBy){

            Bundle queryBundle = new Bundle();
            queryBundle.putString(Constants.SortOrder.KEY, sortBy);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<MovieInfo[]> movieSearch = loaderManager.getLoader(Constants.LoaderID
                    .MainActivity_LOADER_ID);
            if (movieSearch == null) {
                loaderManager.initLoader(Constants.LoaderID.MainActivity_LOADER_ID, queryBundle,
                        new FetchMovieData(this));
            } else {
                loaderManager.restartLoader(Constants.LoaderID.MainActivity_LOADER_ID, queryBundle,
                        new FetchMovieData(this));
            }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(LIST_KEY, movieData);
        outState.putInt(POS_KEY, layoutManager.findFirstVisibleItemPosition());
        outState.putString(SORT_ORDER_KEY, spinner.getSelectedItem().toString());
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
        this.movieData = movieData;
        mainActivityAdapter.setMovieData(movieData);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //handles the drop down menu aka spinner
        MenuItem item = menu.findItem(R.id.spinner_sort_by);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Listens for dropdown menu selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerSelectedItem;
                if(lastSortOrder.isEmpty())
                    spinnerSelectedItem = parent.getItemAtPosition(position).toString();
                else {
                    spinnerSelectedItem = lastSortOrder;
                    if(lastSortOrder == getString(R.string.sortBy_most_popular)){
                        spinner.setSelection(0);
                    }else{
                        spinner.setSelection(1);
                    }

                    lastSortOrder = "";
                }
                if(spinnerSelectedItem.equals(getString(R.string.sortBy_most_popular))){
                 loadMovieData(Constants.SortOrder.POPULAR);
                }else if(spinnerSelectedItem.equals(getString(R.string.sortBy_highest_rated))) {
                 loadMovieData(Constants.SortOrder.TOP_RATED);
                }




            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_item_fav){
            startActivity(new Intent(this, FavActivity.class));

        }

        return super.onOptionsItemSelected(item);

    }
}
