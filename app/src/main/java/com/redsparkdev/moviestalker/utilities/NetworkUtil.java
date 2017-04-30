package com.redsparkdev.moviestalker.utilities;

import android.net.Uri;
import android.util.Log;

import com.redsparkdev.moviestalker.API_KEY;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Red on 30/04/2017.
 */

public final class NetworkUtil {

    //Replace the api key with your key.
    //com.redsparkdev.moviestalker.API_KEY will not be available
    private static final String API_KEY = com.redsparkdev.moviestalker.API_KEY.KEY;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_PARAM = "api_key";
    private static final String TYPE = "movie";

    public static URL buildUrl(String sortBy){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(sortBy)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static final class SortBy {
        public static final String TOP_RATED = "top_rated";
        public static final String POPULAR = "popular";
    }

}
