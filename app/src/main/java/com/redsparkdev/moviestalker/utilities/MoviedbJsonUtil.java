package com.redsparkdev.moviestalker.utilities;

import android.content.Context;

/**
 * Created by Red on 30/04/2017.
 *
 * Util to format and store JSON data into objects
 */

public final class MoviedbJsonUtil{
    public static MovieInfo[] getMovieObjects(Context context, String movieJsonString){

        //JSON keywords
        final String PAGE = "page";
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String OWERVIEW = "overview";
        final String RELEASE_DATE= "release_date";
        final String TITLE = "title";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VOTE_AVERAGE = "vote_average";

        return null;
    }


}
