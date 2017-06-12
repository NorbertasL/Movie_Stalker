package com.redsparkdev.moviestalker;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.redsparkdev.moviestalker.data.FavListContract;
import com.redsparkdev.moviestalker.data.ImageStorage;
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

    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;
    private LinearLayout mainLayout;
    private MovieInfo movieInfo;

    private Button favButton;

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
            if (parentActivity.hasExtra(Constants.ExtraData.OBJECT)) {
                movieInfo = (MovieInfo) parentActivity.getSerializableExtra(Constants
                        .ExtraData.OBJECT);


                //gets the data from the movie object
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);
                titleTextView.setText(movieInfo.getOriginal_title());
                releaseDateTextView.append(movieInfo.getRelease_date());
                ratingTextView.append(movieInfo.getVote_average());
                overviewTextView.setText(movieInfo.getOverview());


                loadTrailerData(movieInfo.getId());
                loadReviewData(movieInfo.getId());

            }
        }
    }


    public MovieInfo getMovieInfoReference() {
        return movieInfo;
    }


    public void showReviews() {
        if(reviewViews.size() > 0){
            return;
        }

        List<ReviewInfo> reviewList = movieInfo.getReviews();
        ReviewInfo [] reviewArray = new ReviewInfo[reviewList.size()];
        reviewList.toArray(reviewArray);

        if(reviewArray.length == 0){
            return;
        }
        for(int i = 0; i < reviewsToShow && i < reviewArray.length ; i++){
            final LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.review_list_item,
                    null);
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
            showMore.setText(getString(R.string.more_comments));

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
        if(trailerViews.size() > 0){
            return;
        }

        final String TYPE_VIDEO = "Trailer";

        LinearLayout trailerField = (LinearLayout) findViewById(R.id.ll_for_trailers);
        for (final TrailerInfo trailer : movieInfo.getTrailers()) {
            //For now only interested in trailers
            if (trailer.getType().equalsIgnoreCase(TYPE_VIDEO)) {
                trailer.setLink(NetworkUtil.buildTrailerVideoUrl(trailer.getKey()));
                final TextView text_view = (TextView) View.inflate(this, R.layout.trailer_list_item,
                        null);

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
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(text_view
                                    .getTag().toString())));
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //TODO set up an error text view;
    public void showError(boolean b) {
       TextView error = (TextView) findViewById(R.id.trailer_error_message_display);

        if(b)
            error.setVisibility(View.VISIBLE);
        else
            error.setVisibility(View.INVISIBLE);


    }

    //TODO set up loading indicator
    public void showLoadingIndicator(boolean b) {
        ProgressBar loading = (ProgressBar) findViewById(R.id.trailer_loading_indicator);

        if(b)
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.INVISIBLE);


    }

    private void loadTrailerData(String id) {

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
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavListContract.FavEntry.COLUMN_MOVIE_ID, movieInfo.getId());
        contentValues.put(FavListContract.FavEntry.COLUMN_OVERVIEW, movieInfo.getOverview());
        contentValues.put(FavListContract.FavEntry.COLUMN_RATING, movieInfo.getVote_average());
        contentValues.put(FavListContract.FavEntry.COLUMN_RELEASE_DATE, movieInfo.getRelease_date());
        contentValues.put(FavListContract.FavEntry.COLUMN_TITLE, movieInfo.getTitle());
        ImageStorage.saveImage(imageImageView.getDrawable(), movieInfo.getId());
        Uri uri = getContentResolver().insert(FavListContract.FavEntry.CONTENT_URI, contentValues);

        if(uri != null){
            Toast.makeText(getBaseContext(), getString(R.string.fov_added), Toast.LENGTH_LONG).show();
        }


    }

}
