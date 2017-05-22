package com.redsparkdev.moviestalker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.redsparkdev.moviestalker.storageObjects.MovieInfo;
import com.redsparkdev.moviestalker.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Red on 30/04/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {

    //Stores all the movie data(not images)
    private MovieInfo[] movieData;

    private MyAdapterOnClickHandler clickHandler;

    public interface MyAdapterOnClickHandler {
        void onClick(MovieInfo movieInfoObject);
    }

    public MyAdapter(MyAdapterOnClickHandler clickHandler){
        this.clickHandler = clickHandler;
    }

    @Override
    public MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutID, parent, shouldAttachToParentImmediately);
        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapterViewHolder holder, int position) {
        String thisMoviesPosterPath = movieData[position].getPoster_path();
        String imgUrl = NetworkUtil.buildImgUrl(thisMoviesPosterPath, NetworkUtil.ImageSize.W185).toString();
        movieData[position].setFull_poster_path(imgUrl);
        //Using Picasso to handle the image
        Picasso.with(holder.holderView.getContext()).load(imgUrl).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        if(movieData == null)
            return 0;
        return movieData.length;
    }
    public class MyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView movieImageView;
        private final View holderView;
        private MyAdapterViewHolder(View view) {
            super(view);
            holderView = view;
            movieImageView = (ImageView) view.findViewById(R.id.image_thumbnail);
            view.setOnClickListener(this);
        }
    @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(movieData[adapterPosition]);

        }
    }
    public void setMovieData(MovieInfo[] movieData){
        this.movieData = movieData;
        notifyDataSetChanged();
    }

}

