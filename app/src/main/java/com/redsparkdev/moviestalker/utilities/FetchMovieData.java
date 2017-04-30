package com.redsparkdev.moviestalker.utilities;

import android.os.AsyncTask;
import com.redsparkdev.moviestalker.MainActivity;

import java.net.URL;


/**
 * Created by Red on 30/04/2017.
 * The AsyncTask to make the data retrieval on separate thread.
 */

public class FetchMovieData extends AsyncTask<String, Void, String[]> {
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
    protected String[] doInBackground(String... params) {
        if(params.length == 0)
            return null;
        String sortBy = params[0];
        //TODO make a url constructor

        return new String[0];
    }

    @Override
    protected void onPostExecute(String[] strings) {
        //Checks is anything was returned
        if(strings !=null){
            mainActivity.showMovieData();
            mainActivity.setMovieData(strings);
        }else{
            //if array is empty shows a generic error
            mainActivity.showErrorMessage();
        }
    }
}
