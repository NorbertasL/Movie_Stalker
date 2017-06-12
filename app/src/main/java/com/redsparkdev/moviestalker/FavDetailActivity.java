package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.redsparkdev.moviestalker.data.FavListContract;
import com.redsparkdev.moviestalker.data.ImageStorage;
import com.redsparkdev.moviestalker.storageObjects.Constants;
import com.redsparkdev.moviestalker.utilities.loaders.localDatabase.FetchFavDetails;

public class FavDetailActivity extends AppCompatActivity {
    private final static String TAG = FavDetailActivity.class.getSimpleName();


    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;
    private Button favButton;
    private int dbID;
    private String movieID;

    //TODO make loading indicator and error message
    //TODO make use of saveState

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_detail);

        titleTextView  = (TextView) findViewById(R.id.fav_title);
        overviewTextView = (TextView) findViewById(R.id.fav_overview);
        ratingTextView = (TextView) findViewById(R.id.fav_rating);
        releaseDateTextView = (TextView) findViewById(R.id.fav_release_date);
        favButton = (Button) findViewById(R.id.fav_button);

        favButton.setText("Remove Favorite");

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO remove from fav and close the activity
                Uri uri = FavListContract.FavEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(String.valueOf(dbID)).build();

                ImageStorage.deleteImage(movieID);
                getContentResolver().delete(uri, null, null);

                finish();
            }
        });


        Intent parentActivity = getIntent();

        //check if extra data was sent via intent
        if (parentActivity != null && savedInstanceState == null) {
            dbID = parentActivity.getIntExtra("ID", -1);
            if(dbID == -1){
                return;
            }

           loadFavData(dbID);
        }


    }



    public void setData(String title, String overview, String releaseDate, String rating, String movieID){
        titleTextView.setText(title);
        overviewTextView.setText(overview);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);
        this.movieID = movieID;
    }
    private void loadFavData(int id){
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("ID", id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> data = loaderManager.getLoader(Constants.LoaderID.FavDetailActivity_LOADER_ID);

        if (data == null) {
            loaderManager.initLoader(Constants.LoaderID.FavDetailActivity_LOADER_ID, queryBundle, new FetchFavDetails(this));
        } else {
            loaderManager.restartLoader(Constants.LoaderID.FavDetailActivity_LOADER_ID, queryBundle, new FetchFavDetails(this));
        }

    }
}
