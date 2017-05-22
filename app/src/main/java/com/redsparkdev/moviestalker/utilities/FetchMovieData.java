package com.redsparkdev.moviestalker.utilities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.redsparkdev.moviestalker.MainActivity;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;

import java.io.IOException;
import java.net.URL;


/**
 * Created by Red on 30/04/2017.
 * The AsyncTask to make the data retrieval on separate thread.
 */

public class FetchMovieData implements LoaderManager.LoaderCallbacks<MovieInfo[]> {

    private final MainActivity mainActivity;

    //We have a constructor just to get access to the Main Activity
    public FetchMovieData(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public Loader<MovieInfo[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieInfo[]>(mainActivity) {

            @Override
            public void onStartLoading(){
                if (args == null) {
                    return;
                }
                mainActivity.showLoadingIndicator();

                forceLoad();
            }

            @Override
            public MovieInfo[] loadInBackground() {
                String sortBy = args.getString(NetworkUtil.SortBy.KEY);
                URL movieRequestUrl = NetworkUtil.buildUrl(sortBy);
                try{
                    String jsonMovieResponse = NetworkUtil.getResponseFromHttpUrl(movieRequestUrl);
                    MovieInfo [] movieData = MoviedbJsonUtil.getMovieObjects(jsonMovieResponse);
                    return movieData;
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieInfo[]> loader, MovieInfo[] data) {
        if(data !=null){
            mainActivity.showMovieData();
            mainActivity.setMovieData(data);
        }else{
            //if array is empty shows a generic error
            mainActivity.showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieInfo[]> loader) {

    }

}
