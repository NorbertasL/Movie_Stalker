package com.redsparkdev.moviestalker.utilities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redsparkdev.moviestalker.R;
import com.redsparkdev.moviestalker.data.FavObject;

/**
 * Created by Red on 05/06/2017.
 */

public class FavActivityAdapter extends RecyclerView.Adapter<FavActivityAdapter.AdapterViewHolder> {

    private FavObject[] favList;

    private AdapterOnClickHandler clickHandler;

    public interface AdapterOnClickHandler{
        void onClick(FavObject favObject);
    }
    public FavActivityAdapter(AdapterOnClickHandler clickHandler){
        this.clickHandler = clickHandler;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.fav_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutID, parent, shouldAttachToParentImmediately);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {

        String title = favList[position].getTitle();
        holder.titleView.setText(title);

    }

    @Override
    public int getItemCount() {
        if(favList == null)
            return 0;
        return favList.length;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titleView;
        private final View holderView;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            holderView = itemView;
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(favList[adapterPosition]);
        }
    }
    public void setFavListData(FavObject[] favListData){
        this.favList = favListData;
        notifyDataSetChanged();
    }
}
