package com.redsparkdev.moviestalker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.redsparkdev.moviestalker.data.FavListContract.FavEntry;

/**
 * Created by Red on 23/05/2017.
 */

public class FavDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favoritesDb.db";
    private static final int VERSION = 3;


    FavDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavEntry.TABLE_NAME + " (" +
                FavEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavEntry.COLUMN_MOVIE_ID + " TEXT, " +
                FavEntry.COLUMN_TITLE    + " TEXT, " +
                FavEntry.COLUMN_RELEASE_DATE + " TEXT, "+
                FavEntry.COLUMN_RATING + " TEXT, "+
                FavEntry.COLUMN_OVERVIEW + " TEXT);";
                //TODO figure out how tro save images


        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavEntry.TABLE_NAME);
        onCreate(db);

    }
}
