package com.ipereirac.simpleandroidrssreader.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ipereirac.simpleandroidrssreader.orm.Entry;

import java.util.ArrayList;
import java.util.List;

public class FeedDBHelper extends SQLiteOpenHelper {
    /**
     * Defines the database version. This variable must be incremented in order for onUpdate to
     * be called when necessary.
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * The name of the database on the device.
     */
    private static final String DATABASE_NAME = "feedList.db";

    /**
     * Default constructor.
     *
     * @param context The application context using this database.
     */
    public FeedDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void saveFeed(Context context, Entry entry) {
        ContentValues values = new ContentValues();
        values.put(FeedContract.FeedEntry.ENTRY_ID, entry.ENTRY_ID);
        values.put(FeedContract.FeedEntry.TITLE, entry.TITLE);
        context.getContentResolver().insert(FeedContract.FeedEntry.CONTENT_URI, values);
    }

    public static List<Entry> getFeeds(Context context) {
        List<Entry> entries = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(FeedContract.FeedEntry.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex(FeedContract.FeedEntry._ID));
                String feed_id = cursor.getString(cursor.getColumnIndex(FeedContract.FeedEntry.ENTRY_ID));
                String  title = cursor.getString(cursor.getColumnIndex(FeedContract.FeedEntry.TITLE));
                Entry entry = new Entry(id, feed_id, title);
                entries.add(entry);
                cursor.moveToNext();
            }
            Log.d("TAG",strBuild.toString());
        } else {
            Log.d("TAG", "No Records Found");
        }

        return entries;
    }

    /**
     * Called when the database is first created.
     *
     * @param db The database being created, which all SQL statements will be executed on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        addFeedTable(db);
    }

    /**
     * Called whenever DATABASE_VERSION is incremented. This is used whenever schema changes need
     * to be made or new tables are added.
     *
     * @param db         The database being updated.
     * @param oldVersion The previous version of the database. Used to determine whether or not
     *                   certain updates should be run.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addFeedTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + FeedContract.FeedEntry.TABLE_NAME + " (" +
                        FeedContract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                        FeedContract.FeedEntry.ENTRY_ID + " TEXT NOT NULL, " +
                        FeedContract.FeedEntry.TITLE + " TEXT NOT NULL);"
        );
    }


}
