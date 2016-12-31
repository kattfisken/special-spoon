package com.example.sara.dinneraid;

import android.provider.BaseColumns;

/**
 * https://developer.android.com/training/basics/data-storage/databases.html
 *
 *  my cook book for databases
 *
 *  It holds names for database columns and table names.
 */
final class ShoppingListContract {

    /**
     * Private constructor so that no one can create instances of the class.
     */
    private ShoppingListContract() {}

    static class ShoppingListLine implements BaseColumns {
        static final String TABLE_NAME = "line";
        static final String COLUMN_NAME_CONTENT ="content";
        static final String COLUMN_NAME_CATEGORY="category";
        static final String COLUMN_NAME_ISDONE="is_done";
    }


}
