package com.redsparkdev.moviestalker;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements MainActivityAdapter.MyAdapterOnClickHandler{

    private final static String TAG = MainActivity.class.getSimpleName();


    private final static String SORT_ORDER_KEY = "sort";




    private RecyclerView recyclerView;
    private TextView errorMessageDisplay;
    private ProgressBar loadingIndicator;
    private MainActivityAdapter mainActivityAdapter;

    private Spinner spinner;
    private int spinnerSavedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Finding an binding ids
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);
        errorMessageDisplay = (TextView)findViewById(R.id.error_message_display);
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);

        int spanCount = 2;//number of columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mainActivityAdapter = new MainActivityAdapter(this);

        recyclerView.setAdapter(mainActivityAdapter);

        /**TODO why does the saveInstantState become null when I navigate to the MovieDetailActivity?
         *the activity does do onSaveInstanceState right after i navigate away from it, but when i check
         *the value after coming back to it it's null.
         **/

    }


    @Override
    public void onClick(MovieInfo movieInfoObject) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

        //Pass in the movieObject class that has all the data the movie
        intent.putExtra(Constants.ExtraData.OBJECT, movieInfoObject);

        //Launch the detail activity to display more details about the movie
        startActivity(intent);

    }

    //Load movie data based on what sorting order the user has selected
    private void  loadMovieData(String sortBy){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constants.SortOrder.KEY, sortBy);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieInfo[]> movieSearch = loaderManager.getLoader(Constants.LoaderID.MainActivity_LOADER_ID);
        if (movieSearch == null) {
            loaderManager.initLoader(Constants.LoaderID.MainActivity_LOADER_ID, queryBundle, new FetchMovieData(this));
        } else {
            loaderManager.restartLoader(Constants.LoaderID.MainActivity_LOADER_ID, queryBundle, new FetchMovieData(this));
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
        mainActivityAdapter.setMovieData(movieData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        spinnerSavedIndex = savedInstanceState.getInt(SORT_ORDER_KEY);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        if(spinner != null)
         outState.putInt(SORT_ORDER_KEY, spinner.getSelectedItemPosition());

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
        if(spinnerSavedIndex != -1){
            spinner.setSelection(spinnerSavedIndex);
            spinnerSavedIndex = -1;
        }

        //Listens for dropdown menu selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String spinnerSelectedItem = parent.getItemAtPosition(position).toString();
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
