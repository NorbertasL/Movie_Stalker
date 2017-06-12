package com.redsparkdev.moviestalker.storageObjects;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Red on 05/06/2017.
 * <p>
 * Stores the Movie Title and DB _ID
 */

public class FavObject implements Parcelable {
    private final String TITLE;
    private final int ID;
    private final int THUMBNAIL_ID;

    public FavObject(String title, int id, int thumbnailID) {
        TITLE = title;
        ID = id;
        THUMBNAIL_ID = thumbnailID;

    }

    public String getTitle() {
        return TITLE;
    }

    public int getID() {
        return ID;
    }

    public int getThumbailID() {
        return THUMBNAIL_ID;
    }


    /**
     * Parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TITLE);
        dest.writeInt(this.ID);
        dest.writeInt(this.THUMBNAIL_ID);
    }

    protected FavObject(Parcel in) {
        this.TITLE = in.readString();
        this.ID = in.readInt();
        this.THUMBNAIL_ID = in.readInt();
    }

    public static final Parcelable.Creator<FavObject> CREATOR = new Parcelable.Creator<FavObject>(){
        @Override
        public FavObject createFromParcel(Parcel source) {
            return new FavObject(source);
        }

        @Override
        public FavObject[] newArray(int size) {
            return new FavObject[size];
        }
    };
}
