package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.redsparkdev.moviestalker.utilities.MovieInfo;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView imageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = (TextView)findViewById(R.id.tv_movie_details_title);
        imageImageView = (ImageView)findViewById(R.id.iv_movie_derails_image);

        Intent parentActivity = getIntent();
        if(parentActivity != null){
            if(parentActivity.hasExtra("MovieInfoObject")){
                MovieInfo movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");
                titleTextView.setText(movieInfo.getTitle());
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);

            }
        }


    }
}
