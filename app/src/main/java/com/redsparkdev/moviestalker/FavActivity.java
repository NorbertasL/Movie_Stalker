package com.redsparkdev.moviestalker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.redsparkdev.moviestalker.data.FavObject;
import com.redsparkdev.moviestalker.storageObjects.Constants;
import com.redsparkdev.moviestalker.utilities.adapters.FavActivityAdapter;
import com.redsparkdev.moviestalker.utilities.loaders.localDatabase.FetchFavList;


public class FavActivity extends AppCompatActivity implements FavActivityAdapter.AdapterOnClickHandler{
    private final String TAG = FavActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private FavActivityAdapter favActivityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_fav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        favActivityAdapter = new FavActivityAdapter(this);

        recyclerView.setAdapter(favActivityAdapter);

        loadFavData();

    }

    private void loadFavData(){
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> data = loaderManager.getLoader(Constants.LoaderID.FavActivity_LOADER_ID);

        if (data == null) {
            loaderManager.initLoader(Constants.LoaderID.FavActivity_LOADER_ID, null, new FetchFavList(this));
        } else {
            loaderManager.restartLoader(Constants.LoaderID.FavActivity_LOADER_ID, null, new FetchFavList(this));
        }

    }


    @Override
    public void onClick(FavObject favObject) {
        //TODO
        Intent intent = new Intent(FavActivity.this, FavDetailActivity.class);
        intent.putExtra("ID", favObject.getID());
        startActivity(intent);

    }
    public void setFavListData(FavObject[] favListData){
        favActivityAdapter.setFavListData(favListData);
    }

}
