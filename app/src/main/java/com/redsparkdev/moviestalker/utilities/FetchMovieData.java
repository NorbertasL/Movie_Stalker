package com.redsparkdev.moviestalker.utilities;

import android.os.AsyncTask;
import com.redsparkdev.moviestalker.MainActivity;

import java.net.URL;


/**
 * Created by Red on 30/04/2017.
 */

public class FetchMovieData extends AsyncTask<String, Void, String[]> {
    private final MainActivity mainActivity;
    public FetchMovieData(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override

    protected void onPreExecute() {
        super.onPreExecute();
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
        if(strings !=null){
            mainActivity.showMovieData();
            mainActivity.setMovieData(strings);
        }else{
            mainActivity.showErrorMessage();
        }
    }
}
