package com.redsparkdev.moviestalker.storageObjects;

/**
 * Created by Red on 30/04/2017.
 * Store all data related to the movie
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Question:Any better way to store data like this or is this acceptable?
 * I was thinking of using a multi-dimensional String array, but the readability would suffer.
 * This just looks a lot cleaner.
 */

public class MovieInfo implements Parcelable, Serializable {


    //making them into empty string instead of null to prevent errors
    private String id = "";
    private String full_poster_path = "";
    private String poster_path = "";
    private String overview = "";
    private String release_date = "";
    private String title = "";
    private String original_title = "";
    private String popularity = "";
    private String vote_count = "";
    private String vote_average = "";

    private List<TrailerInfo> trailers = new ArrayList<>();
    private List<ReviewInfo> reviews = new ArrayList<>();


    //Getters and setters
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getFull_poster_path() {
        return full_poster_path;
    }

    public void setFull_poster_path(String full_poster_path) {
        this.full_poster_path = full_poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void clearTrailerList() {
        trailers.clear();
    }

    public void setTrailers(TrailerInfo[] trailerArray) {
        for (TrailerInfo trailer : trailerArray) {
            this.trailers.add(trailer);
        }
    }

    public List<TrailerInfo> getTrailers() {
        return trailers;
    }

    public void clearReviewList() {
        trailers.clear();
    }

    public void setReviews(ReviewInfo[] reviews) {
        for (ReviewInfo review : reviews) {
            this.reviews.add(review);
        }
    }

    public List<ReviewInfo> getReviews() {
        return reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.full_poster_path);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.title);
        dest.writeString(this.original_title);
        dest.writeString(this.popularity);
        dest.writeString(this.vote_count);
        dest.writeString(this.vote_average);
        dest.writeList(this.trailers);
        dest.writeList(this.reviews);
    }

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        this.id = in.readString();
        this.full_poster_path = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.title = in.readString();
        this.original_title = in.readString();
        this.popularity = in.readString();
        this.vote_count = in.readString();
        this.vote_average = in.readString();
        this.trailers = new ArrayList<TrailerInfo>();
        in.readList(this.trailers, TrailerInfo.class.getClassLoader());
        this.reviews = new ArrayList<ReviewInfo>();
        in.readList(this.reviews, ReviewInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>(){
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
