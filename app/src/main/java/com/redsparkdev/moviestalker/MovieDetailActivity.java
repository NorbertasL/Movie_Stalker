package com.redsparkdev.moviestalker;


import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redsparkdev.moviestalker.data.FavListContract;
import com.redsparkdev.moviestalker.storageObjects.Constants;
import com.redsparkdev.moviestalker.storageObjects.ReviewInfo;
import com.redsparkdev.moviestalker.utilities.loaders.network.FetchMovieReviews;
import com.redsparkdev.moviestalker.utilities.loaders.network.FetchMovieTrailers;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;
import com.redsparkdev.moviestalker.storageObjects.TrailerInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private final static String TAG = MovieDetailActivity.class.getSimpleName();
    private final boolean DEBUG = true;

    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;
    private LinearLayout mainLayout;
    private MovieInfo movieInfo;

    private Button favButton;

    private static final String SAVE_TRAILERS_KEY = "trailers";
    private static final String SAVE_REVIEWS_KEY = "reviews";

    private int reviewsToShow = 3;


    private ArrayList<TextView> trailerViews;
    private ArrayList<LinearLayout> reviewViews;


    private static final int TRAILER_LOADER = 2;
    private static final int REVIEW_LOADER = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trailerViews = new ArrayList<>();
        reviewViews = new ArrayList<>();


        setContentView(R.layout.activity_movie_detail);


        //setting ids
        mainLayout = (LinearLayout) findViewById(R.id.main_movie_detail_layout);
        imageImageView = (ImageView) findViewById(R.id.iv_movie_details_image);
        titleTextView = (TextView) findViewById(R.id.tv_movie_details_title);
        releaseDateTextView = (TextView) findViewById(R.id.tv_movie_details_release_date);
        ratingTextView = (TextView) findViewById(R.id.tv_movie_details_rating);
        overviewTextView = (TextView) findViewById(R.id.tv_movie_details_overview);
        favButton = (Button) findViewById(R.id.button_fav);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav();
            }
        });

        Intent parentActivity = getIntent();

        //check if extra data was sent via intent
        if (parentActivity != null) {
            if (parentActivity.hasExtra("MovieInfoObject")) {
                movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");


                //gets the data from the movie object
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);
                titleTextView.setText(movieInfo.getOriginal_title());
                releaseDateTextView.append(movieInfo.getRelease_date());
                ratingTextView.append(movieInfo.getVote_average());
                overviewTextView.setText(movieInfo.getOverview());


                if (savedInstanceState != null) {
                    if(DEBUG)
                        Log.v(TAG, ":savedInstanceState != null");
                    if (savedInstanceState.getBoolean(SAVE_TRAILERS_KEY)) {
                        showTrailers();
                    } else {
                        loadTrailerData(movieInfo.getId());
                    }

                    if (savedInstanceState.getBoolean(SAVE_REVIEWS_KEY)) {
                        showReviews();
                    } else {
                        loadReviewData(movieInfo.getId());
                    }
                } else {
                    if(DEBUG)
                        Log.v(TAG, ":savedInstanceState == null");
                    loadTrailerData(movieInfo.getId());
                    loadReviewData(movieInfo.getId());
                }

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!movieInfo.getTrailers().isEmpty()) {
            showTrailers();
        } else {
            loadTrailerData(movieInfo.getId());
        }

        if (!movieInfo.getReviews().isEmpty()) {
            showReviews();
        } else {
            loadReviewData(movieInfo.getId());
        }


    }

    public MovieInfo getMovieInfoReference() {
        return movieInfo;
    }


    public void showReviews() {
        if(DEBUG)
            Log.v(TAG, ":showing reviews");

        if(!reviewViews.isEmpty()){
            for (LinearLayout layout : reviewViews) {
                mainLayout.removeView(layout);
            }
        }
        List<ReviewInfo> reviewList = movieInfo.getReviews();
        ReviewInfo [] reviewArray = new ReviewInfo[reviewList.size()];
        reviewList.toArray(reviewArray);

        if(reviewArray.length == 0){
            return;
        }
        for(int i = 0; i < reviewsToShow && i < reviewArray.length ; i++){
            final LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.review_list_item, null);
            TextView author = (TextView) layout.findViewById(R.id.tv_review_author);
            TextView content = (TextView) layout.findViewById(R.id.tv_review_content);
            reviewViews.add(layout);
            author.setText(reviewArray[i].getAuthor());
            content.setText(reviewArray[i].getReview());
            mainLayout.addView(layout);
        }

        //if the button is press it loads 3 more reviews
        if(reviewsToShow < reviewArray.length){
            final Button showMore = new Button(this);
            showMore.setText("Show More Comments");

            mainLayout.addView(showMore);
            showMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    reviewsToShow+=3;
                    showReviews();
                    mainLayout.removeView(showMore);
                }
            });
        }
    }

    public void showTrailers() {

        final String TYPE_VIDEO = "Trailer";

        LinearLayout trailerField = (LinearLayout) findViewById(R.id.ll_for_trailers);
        for (final TrailerInfo trailer : movieInfo.getTrailers()) {
            //For now only interested in trailers
            if (trailer.getType().equalsIgnoreCase(TYPE_VIDEO)) {
                trailer.setLink(NetworkUtil.buildTrailerVideoUrl(trailer.getKey()));
                final TextView text_view = (TextView) View.inflate(this, R.layout.trailer_list_item, null);

                trailerViews.add(text_view);

                text_view.setText(trailer.getName());
                text_view.setTag(trailer.getLink());

                trailerField.addView(text_view);
                //mainLayout.addView(text_view);
                text_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String link = text_view.getTag().toString();
                        if (!link.isEmpty()) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(text_view.getTag().toString())));
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //checking if trailer data was loaded
        if (!movieInfo.getTrailers().isEmpty()) {
            outState.putBoolean(SAVE_TRAILERS_KEY, true);
        } else {
            outState.putBoolean(SAVE_TRAILERS_KEY, false);
        }
        //checking if review data was loaded
        if (!movieInfo.getReviews().isEmpty()) {
            outState.putBoolean(SAVE_REVIEWS_KEY, true);
        } else {
            outState.putBoolean(SAVE_REVIEWS_KEY, false);
        }

        //removing view since they will be rebuilt
        for (TextView view : trailerViews) {
            mainLayout.removeView(view);
        }
        for (LinearLayout layout : reviewViews) {
            mainLayout.removeView(layout);
        }
    }

    //TODO set up an error text view;
    public void showError() {

    }

    //TODO set up loading indicator
    public void showLoadingIndicator() {

    }

    private void loadTrailerData(String id) {
        if(DEBUG)
            Log.v(TAG, ":Loading Trailer data");
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constants.ExtraData.ID_KEY, id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<TrailerInfo[]> trailerSearch = loaderManager.getLoader(TRAILER_LOADER);
        if (trailerSearch == null) {
            loaderManager.initLoader(TRAILER_LOADER, queryBundle, new FetchMovieTrailers(this));
        } else {
            loaderManager.restartLoader(TRAILER_LOADER, queryBundle, new FetchMovieTrailers(this));
        }
    }

    private void loadReviewData(String id) {
        if(DEBUG)
            Log.v(TAG, ":Loading review data");
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constants.ExtraData.ID_KEY, id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ReviewInfo[]> reviewSearch = loaderManager.getLoader(REVIEW_LOADER);
        if (reviewSearch == null) {
            loaderManager.initLoader(REVIEW_LOADER, queryBundle, new FetchMovieReviews(this));
        } else {
            loaderManager.restartLoader(REVIEW_LOADER, queryBundle, new FetchMovieReviews(this));
        }
    }

    private void addToFav(){
        // TODO (7) Insert new task data via a ContentResolver
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavListContract.FavEntry.COLUMN_MOVIE_ID, movieInfo.getId());
        contentValues.put(FavListContract.FavEntry.COLUMN_OVERVIEW, movieInfo.getOverview());
        contentValues.put(FavListContract.FavEntry.COLUMN_RATING, movieInfo.getPopularity());
        contentValues.put(FavListContract.FavEntry.COLUMN_RELEASE_DATE, movieInfo.getRelease_date());
        contentValues.put(FavListContract.FavEntry.COLUMN_TITLE, movieInfo.getTitle());
        Uri uri = getContentResolver().insert(FavListContract.FavEntry.CONTENT_URI, contentValues);

        if(uri != null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        finish();

    }

}
