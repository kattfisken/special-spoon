package com.example.sara.dinneraid;

import android.provider.BaseColumns;

/**
 * https://developer.android.com/training/basics/data-storage/databases.html
 * my cook book for databases
 */
public final class ShoppingListContract {
    private ShoppingListContract() {};

    public static class ShoppingListLine implements BaseColumns {
        public static  final String TABLE_NAME = "line";
        public static  final String COLUMN_NAME_CONTENT ="content";
        public static  final String COLUMN_NAME_CATEGORY="category";
        public static  final String COLUMN_NAME_ISDONE="is_done";
    }


}
