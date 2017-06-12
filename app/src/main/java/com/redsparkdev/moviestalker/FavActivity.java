package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.redsparkdev.moviestalker.storageObjects.FavObject;
import com.redsparkdev.moviestalker.storageObjects.Constants;
import com.redsparkdev.moviestalker.utilities.adapters.FavActivityAdapter;
import com.redsparkdev.moviestalker.utilities.loaders.localDatabase.FetchFavList;


public class FavActivity extends AppCompatActivity
        implements FavActivityAdapter.AdapterOnClickHandler {
    private final static String TAG = FavActivity.class.getSimpleName();
    private final static String LIST_KEY = "list_key";
    private final static String POSSITION_KEY = "list_pos";


    private RecyclerView recyclerView;
    private FavActivityAdapter favActivityAdapter;
    private GridLayoutManager layoutManager;
    private FavObject[] favListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_fav);


        int spanCount = 2;//number of columns
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        favActivityAdapter = new FavActivityAdapter(this);

        recyclerView.setAdapter(favActivityAdapter);

        if (savedInstanceState != null) {
            favListData = (FavObject[]) savedInstanceState.getParcelableArray(LIST_KEY);
            setFavListData(favListData);

            int pos = savedInstanceState.getInt(POSSITION_KEY);
            layoutManager.scrollToPosition(pos);
        } else {
            loadFavData();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (favListData != null) {
            outState.putParcelableArray(LIST_KEY, favListData);
            outState.putInt(POSSITION_KEY, layoutManager.findFirstVisibleItemPosition());
        }
    }

    private void loadFavData() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> data = loaderManager.getLoader(Constants.LoaderID.FavActivity_LOADER_ID);

        if (data == null) {
            loaderManager.initLoader(Constants.LoaderID.FavActivity_LOADER_ID,
                    null, new FetchFavList(this));
        } else {
            loaderManager.restartLoader(Constants.LoaderID.FavActivity_LOADER_ID,
                    null, new FetchFavList(this));
        }

    }


    @Override
    public void onClick(FavObject favObject) {
        //TODO
        Intent intent = new Intent(FavActivity.this, FavDetailActivity.class);
        intent.putExtra("ID", favObject.getID());
        startActivity(intent);

    }

    public void setFavListData(FavObject[] favListData) {
        this.favListData = favListData;
        favActivityAdapter.setFavList(favListData);
        DebugPrint("setFavListData");

    }

    private static void DebugPrint(String s) {
        Log.v(TAG, s);
    }

}
