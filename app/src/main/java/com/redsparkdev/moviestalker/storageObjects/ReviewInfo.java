package com.redsparkdev.moviestalker.storageObjects;

/**
 * Created by Red on 22/05/2017.
 */

public class ReviewInfo {
    private String author;
    private String review;

    public ReviewInfo(String author, String review){
        this.author = author;
        this.review = review;

    }
    public String getAuthor(){
        return author;
    }
    public String getReview(){
        return review;
    }
}
