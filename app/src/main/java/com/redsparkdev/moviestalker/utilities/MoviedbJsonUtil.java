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
    final static String TAG = MoviedbJsonUtil.class.getSimpleName();

    final static String RESULTS = "results";

    public static TrailerInfo[] getTrailerObjects(String trailerJsonString){

        final String NAME = "name";
        final String KEY = "key";
        final String SITE = "site";

        TrailerInfo[] trailers;
        try{
            JSONObject trailerJson = new JSONObject((trailerJsonString));
            JSONArray trailerArray = trailerJson.getJSONArray(RESULTS);
            trailers = new TrailerInfo[trailerArray.length()];
            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailerInfoJson = trailerArray.getJSONObject(i);

                trailers[i] = new TrailerInfo();
                trailers[i].setName(trailerInfoJson.getString(NAME));
                trailers[i].setKey(trailerInfoJson.getString(KEY));
                trailers[i].setSite(trailerInfoJson.getString(SITE));

            }
            return trailers;

        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static MovieInfo[] getMovieObjects(String movieJsonString){


        //JSON keywords
        final String ID = "id";
        final String STATUS_CODE = "status_code";//this indicates an error1
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE= "release_date";
        final String TITLE = "title";
        final String ORIGINAL_TITLE="original_title";
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

                //Assigning the strings to the object
                movies[i] = new MovieInfo();
                movies[i].setId(movieInfoJson.getString(ID));
                movies[i].setPoster_path(movieInfoJson.getString(POSTER_PATH));
                movies[i].setTitle(movieInfoJson.getString(TITLE));
                movies[i].setOverview(movieInfoJson.getString(OVERVIEW));
                movies[i].setRelease_date(movieInfoJson.getString(RELEASE_DATE));
                movies[i].setPopularity(movieInfoJson.getString(POPULARITY));
                movies[i].setVote_count(movieInfoJson.getString(VOTE_COUNT));
                movies[i].setVote_average(movieInfoJson.getString(VOTE_AVERAGE));
                movies[i].setOriginal_title(movieInfoJson.getString(ORIGINAL_TITLE));
                movies[i].setOriginal_title(movieInfoJson.getString(ORIGINAL_TITLE));
            }
            return movies;

        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }


}
