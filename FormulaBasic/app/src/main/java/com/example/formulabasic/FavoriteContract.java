package com.example.formulabasic;

import android.provider.BaseColumns;

public final class FavoriteContract {
    private FavoriteContract() {}

    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_ITEM_ID = "item_id";
        public static final String COLUMN_NAME_ITEM_TYPE = "item_type";
        public static final String COLUMN_NAME_ITEM_NAME = "item_name";
    }
}
