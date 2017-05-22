package com.redsparkdev.moviestalker.utilities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.redsparkdev.moviestalker.MovieDetailActivity;
import com.redsparkdev.moviestalker.storageObjects.TrailerInfo;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Red on 19/05/2017.
 * Class responsible for handling movie trailer requests
 */

public class FetchMovieTrailers implements LoaderManager.LoaderCallbacks<TrailerInfo[]>{
    private final static String TAG = FetchMovieTrailers.class.getSimpleName().toString();

    //AppCompatActivity is a super class of MovieDerailActivity;
    private final AppCompatActivity callerClass;
    private final MovieDetailActivity movieDetailActivity;
    public FetchMovieTrailers(AppCompatActivity callerClass){
        this.callerClass = callerClass;
        this.movieDetailActivity = (MovieDetailActivity)callerClass;
    }

    @Override
    //Bundle args should contain id of the movie
    public Loader<TrailerInfo[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<TrailerInfo[]>(callerClass) {


            @Override
            public void onStartLoading(){
                //no id, so nothing to do
                if (args == null) {
                    return;
                }
                //Clear the list so we don't have duplicates
                movieDetailActivity.getMovieInfoReference().clearTrailerList();
                //TODO create a loading indicator for data

                //TODO Not sure why forceLoad() in needed.Find out
                forceLoad();
            }

            @Override
            public TrailerInfo [] loadInBackground() {
                //gets the id and stores in in a string
                String id = args.getString(NetworkUtil.ExtraData.ID_KEY);

                //requests a URL for the id
                URL trailerListUrl = NetworkUtil.buildTrailerListUrl(id);
                try{
                    //gets the raw json
                    String jsonTrailerListResponse = NetworkUtil.getResponseFromHttpUrl(trailerListUrl);
                    Log.v(TAG, jsonTrailerListResponse);

                    //uses the MoviedbJsonUtil to sort and store the json data in a TrailerInfo object
                    TrailerInfo [] trailers = MoviedbJsonUtil.getTrailerObjects(jsonTrailerListResponse);
                    return trailers;
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<TrailerInfo[]> loader, TrailerInfo[] data) {


        if(data != null){

            Log.v(TAG, data.toString());
            //set the trailer to the appropriate movie.
            movieDetailActivity.getMovieInfoReference().setTrailers(data);

            //displayed the trailers
            movieDetailActivity.showTrailers();
        }else{
            movieDetailActivity.showError();

        }
    }

    @Override
    public void onLoaderReset(Loader<TrailerInfo[]> loader) {

    }
}
