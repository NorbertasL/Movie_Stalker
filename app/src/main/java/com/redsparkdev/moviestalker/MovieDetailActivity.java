package com.redsparkdev.moviestalker;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redsparkdev.moviestalker.storageObjects.ReviewInfo;
import com.redsparkdev.moviestalker.utilities.FetchMovieReviews;
import com.redsparkdev.moviestalker.utilities.FetchMovieTrailers;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;
import com.redsparkdev.moviestalker.storageObjects.TrailerInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//TODO fix duplication of Trailers and Reviews
public class MovieDetailActivity extends AppCompatActivity {
    private final static String TAG = MovieDetailActivity.class.getSimpleName().toString();
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;
    private LinearLayout mainLayout;
    private MovieInfo movieInfo;

    private static final String SAVE_NAME_KEY = "name";
    private static final String SAVE_LINK_KEY = "link";

    private static ArrayList<String> trailer_Name;
    private static ArrayList<String> trailer_Link;

    private static final String SAVE_AUTHOR_KEY = "author";
    private static final String SAVE_CONTENT_KEY = "content";

    private static ArrayList<String> reviews_Author;
    private static ArrayList<String> review_Content;

    private  ArrayList<TextView> trailerViews;
    private  ArrayList<LinearLayout> reviewViews;




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

        Intent parentActivity = getIntent();

        //check if extra data was sent via intent
        if (parentActivity != null) {
            if (parentActivity.hasExtra("MovieInfoObject")) {
                movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");
                loadTrailerData(movieInfo.getId());



                //gets the data from the movie object
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);
                titleTextView.setText(movieInfo.getOriginal_title());
                releaseDateTextView.append(movieInfo.getRelease_date());
                ratingTextView.append(movieInfo.getVote_average());
                overviewTextView.setText(movieInfo.getOverview());

            }
        }
    }

    public MovieInfo getMovieInfoRefrance() {
        return movieInfo;
    }



    public void showReviews(){
        Log.v(TAG, ":showing reviews");
        for(ReviewInfo review: movieInfo.getReviews()){
            final LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.review_list_item, null);
            TextView autor = (TextView)layout.findViewById(R.id.tv_review_author);
            TextView content = (TextView) layout.findViewById(R.id.tv_review_content);
            reviewViews.add(layout);
            autor.setText(review.getAuthor());
            content.setText(review.getReview());

            mainLayout.addView(layout);

        }


    }

    //DONE TO-DO need to created an appropriate number of text views with the trailer names
    public void showTrailers() {

        final String TYPE_VIDEO = "Trailer";

        //trailer_Name = new ArrayList<>();
       // trailer_Link = new ArrayList<>();

        for (final TrailerInfo trailer : movieInfo.getTrailers()) {
            //For now only interested in trailers
            if (trailer.getType().equalsIgnoreCase(TYPE_VIDEO)) {
                trailer.setLink(NetworkUtil.buildTrailerVideoUrl(trailer.getKey()));
                final TextView text_view = (TextView) View.inflate(this, R.layout.trailer_list_item, null) ;
                trailerViews.add(text_view);


                text_view.setText(trailer.getName());
                text_view.setTag(trailer.getLink());

                /** i find no need for this
                if (!trailer.getName().isEmpty())
                    trailer_Name.add(trailer.getName());
                else
                    trailer_Name.add("Name not found");
                if (!trailer.getLink().toString().isEmpty())
                    trailer_Link.add(trailer.getLink().toString());
                else
                    trailer_Link.add("no link");

                 **/
                mainLayout.addView(text_view);
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
        //Making sure review load after the trailers :).
        loadReviewData(movieInfo.getId());

    }


    //TODO set up an error text view;
    public void showError() {

    }
    //TODO set up loading indicator
    public void showLoadingIndicator() {

    }

    private void loadTrailerData(String id) {
        Log.v(TAG, ":Loading Trailer data");
        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtil.ExtraData.ID_KEY, id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<TrailerInfo[]> trailerSearch = loaderManager.getLoader(TRAILER_LOADER);
        if (trailerSearch == null) {
            loaderManager.initLoader(TRAILER_LOADER, queryBundle, new FetchMovieTrailers(this));
        } else {
            loaderManager.restartLoader(TRAILER_LOADER, queryBundle, new FetchMovieTrailers(this));
        }
    }
    private void loadReviewData(String id){
        Log.v(TAG, ":Loading review data");
        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtil.ExtraData.ID_KEY, id);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ReviewInfo[]> reviewSearch = loaderManager.getLoader(REVIEW_LOADER);
        if(reviewSearch == null){
            loaderManager.initLoader(REVIEW_LOADER, queryBundle, new FetchMovieReviews(this));
        }else{
            loaderManager.restartLoader(REVIEW_LOADER, queryBundle, new FetchMovieReviews(this));
        }
    }

}
