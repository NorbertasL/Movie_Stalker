package com.redsparkdev.moviestalker.utilities;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Red on 30/04/2017.
 *
 * Util to format and store JSON data into objects
 */

public final class MoviedbJsonUtil{
    public static MovieInfo[] getMovieObjects(String movieJsonString){
        final String TAG = MoviedbJsonUtil.class.getSimpleName();

        //JSON keywords
        final String STATUS_CODE = "status_code";//this indicates an error1
        final String PAGE = "page";
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String OWERVIEW = "overview";
        final String RELEASE_DATE= "release_date";
        final String TITLE = "title";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VOTE_AVERAGE = "vote_average";

        MovieInfo[] movies;

        try {
            JSONObject movieJson = new JSONObject((movieJsonString));
            if(movieJson.has(STATUS_CODE)){
                Log.v(TAG, "Error:"+ movieJson.getInt(STATUS_CODE));
                return null;
            }
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);

            movies = new MovieInfo[movieArray.length()];

            for(int i = 0; i < movieArray.length(); i++){
                JSONObject movieInfoJson = movieArray.getJSONObject(i);
                //Log.v(TAG, ":"+movieInfoJson.getString(TITLE));
                movies[i] = new MovieInfo();
                movies[i].setPoster_path(movieInfoJson.getString(POSTER_PATH));
                movies[i].setTitle(movieInfoJson.getString(TITLE));

            }

            return movies;

        }catch(JSONException e){
            //TODO handle exception
            Log.e(TAG, e.toString());
        }
        return null;
    }


}
