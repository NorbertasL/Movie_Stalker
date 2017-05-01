package com.redsparkdev.moviestalker.utilities;

import android.os.AsyncTask;
import com.redsparkdev.moviestalker.MainActivity;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Red on 30/04/2017.
 * The AsyncTask to make the data retrieval on separate thread.
 */

public class FetchMovieData extends AsyncTask<String, Void, MovieInfo[]> {

    private final MainActivity mainActivity;

    //We have a constructor just to get access to the Main Activity
    public FetchMovieData(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Shows a loading indicator on the screen
        mainActivity.showLoadingIndicator();
    }

    @Override
    protected MovieInfo[] doInBackground(String... params) {
        if(params.length == 0)
            return null;
        String sortBy = params[0];
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

    @Override
    protected void onPostExecute(MovieInfo[] movies) {
        //Checks is anything was returned
        if(movies !=null){
            mainActivity.showMovieData();
            mainActivity.setMovieData(movies);
        }else{
            //if array is empty shows a generic error
            mainActivity.showErrorMessage();
        }
    }
}
