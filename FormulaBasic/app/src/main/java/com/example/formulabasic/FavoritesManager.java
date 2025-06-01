package com.example.formulabasic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private FavoriteDbHelper dbHelper;

    public FavoritesManager(Context context) {
        dbHelper = new FavoriteDbHelper(context);
    }

    public boolean addFavorite(String itemId, String itemType, String itemName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID, itemId);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_TYPE, itemType);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_NAME, itemName);

        long newRowId = db.insertWithOnConflict(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE);

        return newRowId != -1;
    }

    public boolean removeFavorite(String itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID + " LIKE ?";
        String[] selectionArgs = { itemId };
        int deletedRows = db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    public boolean isFavorite(String itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID
        };
        String selection = FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID + " = ?";
        String[] selectionArgs = { itemId };

        Cursor cursor = db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        boolean isFav = cursor.getCount() > 0;
        cursor.close();
        return isFav;
    }

    public List<FavoriteItem> getAllFavorites() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<FavoriteItem> favorites = new ArrayList<>();

        String[] projection = {
                FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID,
                FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_TYPE,
                FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_NAME
        };

        Cursor cursor = db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_ID));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_TYPE));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteEntry.COLUMN_NAME_ITEM_NAME));
            favorites.add(new FavoriteItem(itemId, itemType, itemName));
        }
        cursor.close();
        return favorites;
    }
}