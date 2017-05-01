package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.redsparkdev.moviestalker.utilities.MovieInfo;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //setting ids
        imageImageView = (ImageView)findViewById(R.id.iv_movie_details_image);
        titleTextView = (TextView)findViewById(R.id.tv_movie_details_title);
        releaseDateTextView = (TextView)findViewById(R.id.tv_movie_details_release_date);
        ratingTextView = (TextView)findViewById(R.id.tv_movie_details_rating);
        overviewTextView = (TextView)findViewById(R.id.tv_movie_details_overview);

        Intent parentActivity = getIntent();

        //check if extra data was sent via intent
        if(parentActivity != null){
            if(parentActivity.hasExtra("MovieInfoObject")){
                MovieInfo movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");

                //gets the data from the movie object
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);
                titleTextView.setText(movieInfo.getOriginal_title());
                releaseDateTextView.append(movieInfo.getRelease_date());
                ratingTextView.append(movieInfo.getVote_average());
                overviewTextView.setText(movieInfo.getOverview());
            }
        }
    }
}
