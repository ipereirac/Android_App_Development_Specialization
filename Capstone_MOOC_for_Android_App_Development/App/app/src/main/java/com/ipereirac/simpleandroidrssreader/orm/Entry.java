package com.ipereirac.simpleandroidrssreader.orm;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry  implements Parcelable {

    public long _ID;
    public String ENTRY_ID;
    public String TITLE;


    protected Entry(Parcel in) {
        _ID = in.readLong();
        ENTRY_ID = in.readString();
        TITLE = in.readString();
    }

    public Entry(long _ID, String ENTRY_ID, String TITLE) {
        this._ID = _ID;
        this.TITLE = TITLE;
        this.ENTRY_ID = ENTRY_ID;
    }

    public Entry(String ENTRY_ID, String TITLE) {
        this._ID = -1L;
        this.TITLE = TITLE;
        this.ENTRY_ID = ENTRY_ID;
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_ID);
        dest.writeString(ENTRY_ID);
        dest.writeString(TITLE);
    }
}
