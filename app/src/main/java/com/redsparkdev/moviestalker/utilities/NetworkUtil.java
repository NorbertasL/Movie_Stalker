package com.redsparkdev.moviestalker.utilities;

import android.net.Uri;
import android.util.Log;

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
    //TODO add your API key
    private final static String TAG = NetworkUtil.class.getSimpleName().toString();

    private static final String API_KEY = com.redsparkdev.moviestalker.API_KEY.KEY;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_PARAM = "api_key";
    private static final String TYPE = "movie";

    public static URL buildReviewListUrl(String id){
        final String REVIEWS = "reviews";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(id)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        Log.v(TAG, buildUri.toString());
        return UrlBuild(buildUri);

    }

    //Build URL for the youtube video link for the trailer
    public static URL buildTrailerVideoUrl(String key){
        final String BASE = "https://www.youtube.com/watch";
        final String PAR = "v";
        Uri buildUri = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(PAR, key)
                .build();
        Log.v(TAG, buildUri.toString());
        return UrlBuild(buildUri);
    }

    //Build the URL for retrieves trailer list
    public static URL buildTrailerListUrl(String id){
        final String VIDEOS = "videos";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(id)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        Log.v(TAG, buildUri.toString());
        return UrlBuild(buildUri);
    }
    public static URL buildUrl(String sortBy){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(sortBy)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        return UrlBuild(builtUri);
    }
    public static URL buildImgUrl(String imgPath, String imgSize){
        final String BASE_URL = "http://image.tmdb.org/t/p";
        final StringBuilder path = new StringBuilder(imgPath);
        path.deleteCharAt(0);

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(imgSize)
                .appendPath(path.toString())
                .build();

        return UrlBuild(builtUri);
    }
    private static URL UrlBuild(Uri uri){
        URL url = null;
        try{
            url = new URL(uri.toString());
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
        public static final String KEY = "sortBy";
        public static final String TOP_RATED = "top_rated";
        public static final String POPULAR = "popular";
    }
    public static final class ExtraData{
        public static final String ID_KEY = "id";
        public static final String TRILERS = "videos";
        public static final String REVIEWS = "reviews";
    }
    public static final class ImageSize{
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
        public static final String ORIGINAL = "original";


    }

}
