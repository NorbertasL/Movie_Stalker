package com.redsparkdev.moviestalker.storageObjects;

/**
 * Created by Red on 06/06/2017.
 * Class to store constants
 */

public final class Constants {

    /**
     * Using this class to set all loader ids in one place,
     * this prevent me from accidentally using the same id twice
     */
    public final class LoaderID {
        public static final int MainActivity_LOADER_ID = 1;
        public static final int FavActivity_LOADER_ID = 2;
        public static final int FavDetailActivity_LOADER_ID = 3;
    }

    public static final class SortOrder {
        public static final String KEY = "sortBy";
        public static final String TOP_RATED = "top_rated";
        public static final String POPULAR = "popular";
    }

    /**
     * Some of the constants are not used
     * Left them in for future update on the app
     */
    public static final class ExtraData {
        public static final String ID_KEY = "id";
        public static final String TRILERS = "videos";
        public static final String REVIEWS = "reviews";

        public static final String OBJECT = "object";
    }

    public static final class ImageSize {
        public static final String W92 = "w92";
        public static final String W154 = "w154";
        public static final String W185 = "w185";
        public static final String W342 = "w342";
        public static final String W500 = "w500";
        public static final String W780 = "w780";
        public static final String ORIGINAL = "original";


    }
}
