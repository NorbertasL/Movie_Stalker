package com.redsparkdev.moviestalker.utilities.loaders.localDatabase;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.redsparkdev.moviestalker.FavActivity;
import com.redsparkdev.moviestalker.data.FavListContract;
import com.redsparkdev.moviestalker.storageObjects.FavObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Red on 05/06/2017.
 */

public class FetchFavList implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = FetchFavList.class.getSimpleName();

    private FavActivity activity;


    public FetchFavList(FavActivity activity) {
        this.activity = activity;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


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
                    return activity.getContentResolver().query(FavListContract.FavEntry.CONTENT_URI,
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


        int columTitleID = data.getColumnIndex(FavListContract.FavEntry.COLUMN_TITLE);
        int columIdID = data.getColumnIndex(FavListContract.FavEntry._ID);
        int columMovieIdID = data.getColumnIndex(FavListContract.FavEntry.COLUMN_MOVIE_ID);

        List<FavObject> favList = new ArrayList<>();
        while (data.moveToNext()) {
            favList.add(new FavObject(data.getString(columTitleID), data.getInt(columIdID),
                    data.getInt(columMovieIdID)));
        }
        FavObject[] temp = new FavObject[favList.size()];
        favList.toArray(temp);

        activity.setFavListData(temp);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static void myDebugPrint(String s) {
        Log.v(TAG, ":" + s);
    }
}

