package com.redsparkdev.moviestalker.data;

/**
 * Created by Red on 05/06/2017.
 *
 * Stores the Movie Title and DB _ID
 */

public class FavObject {
    private final String TITLE;
    private final int ID;

    public FavObject(String title, int id ){
        TITLE = title;
        ID = id;
    }
    public String getTitle(){
        return TITLE;
    }
    public int getID(){
        return ID;
    }
}
