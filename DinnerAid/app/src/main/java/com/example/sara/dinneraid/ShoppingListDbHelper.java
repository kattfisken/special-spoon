package com.example.sara.dinneraid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * a helper class for accessing the db
 * some help collected from here http://www.vogella.com/tutorials/AndroidSQLite/article.html#introduction-to-the-project
 */
public class ShoppingListDbHelper extends SQLiteOpenHelper {


    private static final String TEXT_TYPE = " TEXT";
    /**
     * there is no bool type. i will use ints with 0 as false, and all other as true
     * regarding booleans: https://www.sqlite.org/datatype3.html
     */
    private static final String BOOL_TYPE = " INTEGER DEFAULT 0";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ShoppingListContract.ShoppingListLine.TABLE_NAME + " (" +
                    ShoppingListContract.ShoppingListLine._ID + " INTEGER PRIMARY KEY," +
                    ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE + BOOL_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShoppingListContract.ShoppingListLine.TABLE_NAME;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingList.db";

    public ShoppingListDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Constants.LOG_TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which destroys all old data!");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
