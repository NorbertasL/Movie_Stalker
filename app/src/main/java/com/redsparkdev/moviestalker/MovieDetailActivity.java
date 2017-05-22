package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.redsparkdev.moviestalker.utilities.FetchMovieTrailers;
import com.redsparkdev.moviestalker.utilities.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;
import com.redsparkdev.moviestalker.utilities.TrailerInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private final static String TAG = MovieDetailActivity.class.getSimpleName().toString();
    private ImageView imageImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;
    private LinearLayout mainLayout;

    private MovieInfo movieInfo;

    private static final int TRAILER_LOADER = 2;
    private static final String LINK_KEY = "video_link";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_detail);



        //setting ids
        mainLayout = (LinearLayout)findViewById(R.id.main_movie_detail_layout);
        imageImageView = (ImageView)findViewById(R.id.iv_movie_details_image);
        titleTextView = (TextView)findViewById(R.id.tv_movie_details_title);
        releaseDateTextView = (TextView)findViewById(R.id.tv_movie_details_release_date);
        ratingTextView = (TextView)findViewById(R.id.tv_movie_details_rating);
        overviewTextView = (TextView)findViewById(R.id.tv_movie_details_overview);

        Intent parentActivity = getIntent();

        //check if extra data was sent via intent
        if(parentActivity != null){
            if(parentActivity.hasExtra("MovieInfoObject")){
                movieInfo = (MovieInfo) parentActivity.getSerializableExtra("MovieInfoObject");


                //gets the data from the movie object
                Picasso.with(this).load(movieInfo.getFull_poster_path()).into(imageImageView);
                titleTextView.setText(movieInfo.getOriginal_title());
                releaseDateTextView.append(movieInfo.getRelease_date());
                ratingTextView.append(movieInfo.getVote_average());
                overviewTextView.setText(movieInfo.getOverview());

                showTrailers();
            }
        }
    }
    public MovieInfo getMovieInfoRefrance(){
        return movieInfo;
    }

    //TODO need to created an appropriate number of text views with the trailer names
    public void showTrailers() {
        //no data in class?Download it
        if (movieInfo.getTrailers().isEmpty()) {
            loadTrailerData(movieInfo.getId());
        }

        //List<String> names = new ArrayList<String>();
        //List<String> keys = new ArrayList<String>();

        //List<TextView> tailer_text_view =  new ArrayList<>();

        for(TrailerInfo trailer : movieInfo.getTrailers()){

            final TextView text_view = new TextView(this);
            text_view.setText(trailer.getName());
            text_view.setTag(trailer.getKey());
            mainLayout.addView(text_view);


            //names.add(trailer.getName());
            //keys.add(trailer.getKey());

        }


        /**
        ListView view = (ListView) findViewById(R.id.lv_movie_details_trailers);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        view.setAdapter(arrayAdapter);
         **/




    }

    //TODO set up an error text view;
    public void showError(){

    }
    private void loadTrailerData(String id){
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
}
