package com.example.mtd319_eca_carsontham;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ListingDatabase extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;

    public ListingDatabase(@Nullable Context context) {
        super(context, "listing.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TBL_LISTING (LISTINGID INTEGER PRIMARY KEY, LISTING_OWNER INTEGER, NAME TEXT, CATEGORY TEXT, DESCRIPTION TEXT, IMAGE BLOB, REQUESTED_BY INTEGER, TRANSACTION_STATUS TEXT);");
        db.execSQL("CREATE TABLE TBL_USERS (USERID INTEGER PRIMARY KEY, NAME TEXT, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS TBL_LISTING");
            //db.execSQL("DROP TABLE IF EXISTS TBL_USERS");
            onCreate(db);
        }
    }
}
