package com.redsparkdev.moviestalker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Red on 24/05/2017.
 */

public class FavContentProvider extends ContentProvider{
    private final static String TAG = FavContentProvider.class.getSimpleName();

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID =  101;
    //private static final int FAVORITE_TITLE = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavListContract.AUTHORITY, FavListContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(FavListContract.AUTHORITY, FavListContract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
        //uriMatcher.addURI(FavListContract.AUTHORITY, FavListContract.PATH_FAVORITES + "/" +FavListContract.FavEntry.COLUMN_TITLE, FAVORITE_TITLE);

        return uriMatcher;
    }

    private FavDbHelper mFavDbHelper;

    @Override
    public boolean onCreate() {
        mFavDbHelper = new FavDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match){
            case FAVORITES:
                cursor = db.query(FavListContract.FavEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(FavListContract.FavEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FAVORITES:
                long id = db.insert(FavListContract.FavEntry.TABLE_NAME, null, values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(FavListContract.FavEntry.CONTENT_URI, id);
                }else{

                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                db.delete(FavListContract.FavEntry.TABLE_NAME,
                        "_id=?",
                        new String[]{id} );

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public void myDebugPrint(String s){
        Log.v(TAG, ":"+s);
    }
}
