package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.redsparkdev.moviestalker.utilities.MovieInfo;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = (TextView)findViewById(R.id.tv_movie_details_title);
        Intent parentActivity = getIntent();
        if(parentActivity != null){
            if(parentActivity.hasExtra("MovieInfoObject")){
                MovieInfo movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");
                titleTextView.setText(movieInfo.getTitle());

            }
        }


    }
}
