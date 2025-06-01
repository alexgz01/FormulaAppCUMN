package com.example.formulabasic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Favorites.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID + " TEXT UNIQUE," +
                    FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_TYPE + " TEXT," +
                    FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
