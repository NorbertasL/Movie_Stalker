package com.redsparkdev.moviestalker.utilities.loaders.localDatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.redsparkdev.moviestalker.FavDetailActivity;
import com.redsparkdev.moviestalker.data.FavListContract;

/**
 * Created by Red on 06/06/2017.
 */

public class FetchFavDetails implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = FetchFavDetails.class.getSimpleName();

    private FavDetailActivity activity;
    int movieID;

    public FetchFavDetails(FavDetailActivity activity){
        this.activity = activity;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(args != null){
            movieID = args.getInt("ID");
            myDebugPrint("args:"+movieID);
        }else{
            myDebugPrint("args null");
            return null;

        }

        return new AsyncTaskLoader<Cursor>(activity) {


            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {

                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);

                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to cat
                //
                //ch any errors in loading data

                try {
                    return activity.getContentResolver().query(FavListContract.FavEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieID)).build(),
                                null,
                                null,
                                null,
                                null);




                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if(data.moveToNext()) {
            myDebugPrint(data.toString());
            String title = data.getString(data.getColumnIndex(FavListContract.FavEntry.COLUMN_TITLE));
            String overview = data.getString(data.getColumnIndex(FavListContract.FavEntry.COLUMN_OVERVIEW));
            String rating = data.getString(data.getColumnIndex(FavListContract.FavEntry.COLUMN_RATING));
            String releaseDate = data.getString(data.getColumnIndex(FavListContract.FavEntry.COLUMN_RELEASE_DATE));
            myDebugPrint("Title:" + title);
            myDebugPrint("Overview:" + overview);
            activity.setData(title, overview, releaseDate, rating);

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public static void myDebugPrint(String s){
        Log.v(TAG, ":"+s);
    }
}
