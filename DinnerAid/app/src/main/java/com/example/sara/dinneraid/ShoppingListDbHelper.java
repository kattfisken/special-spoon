package com.example.sara.dinneraid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FilterQueryProvider;

/**
 * a helper class for accessing the db
 * <p>
 * some help collected from here
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html#introduction-to-the-project
 * <p>
 * This implementation is state-ful and very much not thread safe. This is because all the read-
 * and write-methods on the heldper needs the state of the mDb
 * member to be set accordingly..
 * <p>
 * The class handles opening and closing, reading and writing to the database.
 */
class ShoppingListDbHelper extends SQLiteOpenHelper {

    // below are a handful of SQL statements.

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
                    ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE + BOOL_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShoppingListContract.ShoppingListLine.TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingList.db";
    private static final String[] LINE_PROJECTION = {
            ShoppingListContract.ShoppingListLine._ID,
            ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT,
            ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY,
            ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE
    };
    private static final String LINE_SORT_ORDER =
            ShoppingListContract.ShoppingListLine._ID + " DESC";
    private SQLiteDatabase mDb;
    private static final String SQL_WHERE_DROP_DONE_LINES =
            ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE + " != 0";


    /**
     * constructor method for making a new helper. normally simply opening a readable database.
     * set a flag inside the method if you want this method do reset the database content
     * which is suitable when you are developing and debugging :)
     *
     * @param context not used.
     */
    ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        // this block is for resetting the database.
        // it has been very useful in development and debugging, so i will keep it as below.
        // For future debugging, simply set the boolean resetTheDatabase to true.
        boolean resetTheDatabase = false;
        //noinspection ConstantConditions
        if (resetTheDatabase) {
            String[] s1 = {"1 l mjölk", "300g nötkött", "3 röda paprikor", "5 gurkor"};
            String[] s2 = {"Diary", "Meat", "Vegetables", "Vegetables"};
            openWritableDatabase();
            Log.v(Toolbox.LOG_TAG, "opened the db for writing");
            dropLinesFromTable(null);
            Log.v(Toolbox.LOG_TAG, "cleared the line table - i.e. destroyed all old data.");
            for (int j = 0; j < s1.length; j++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT, s1[j]);
                contentValues.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY, s2[j]);
                insert(ShoppingListContract.ShoppingListLine.TABLE_NAME, null, contentValues);
            }
            Log.v(Toolbox.LOG_TAG, "loaded the db with default records");
            closeDb();
        }

        openReadableDatabase();
    }

    /**
     * the callback for creating a new database.
     *
     * @param db the database to create
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.v(Toolbox.LOG_TAG, "Created the table " + ShoppingListContract.ShoppingListLine.TABLE_NAME);
    }

    /**
     * method for upgrading the sqlite db schema version.
     *
     * @param db         the db to downgrade
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Toolbox.LOG_TAG,
                "Up-/downgrading database from version " + oldVersion + " to "
                        + newVersion + ", which destroys all old data!");
        dropLineTable(db);
        onCreate(db);
    }

    /**
     * method for reverting the sqlite db schema version.
     *
     * @param db         the db to downgrade
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * drop the whole table for shopping lines.
     *
     * @param db the db to drop the table from. typically the mDb member.
     */
    private void dropLineTable(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    /**
     * make sure to call this method before calling the reading-clauses.
     * one can cosinder refactoring so that this is called automatically when trying to read the db.
     */
    private void openReadableDatabase() {
        mDb = getReadableDatabase();
    }

    /**
     * Delete lines from the table
     *
     * @param whereClause the where clause to add. Omit the 'WHERE' statement. Leave null if
     *                    all lines are to be dropped.
     */
    private int dropLinesFromTable(@Nullable String whereClause) {
        return mDb.delete(ShoppingListContract.ShoppingListLine.TABLE_NAME, whereClause, null);
    }

    /**
     * open a writable db. make sure to call it before insert() or dropLinesFromTable or similar
     */
    private void openWritableDatabase() {
        mDb = getWritableDatabase();
    }

    /**
     * A method for calling insert() the otherwise private database.
     * make sure to call openWritableDatabase() before using this method.
     *
     * @param tableName      see SQLiteDataBase.insert(...)
     * @param nullColumnHack see SQLiteDataBase.insert(...)
     * @param contentValues  see SQLiteDataBase.insert(...)
     */
    private void insert(String tableName, String nullColumnHack, ContentValues contentValues) {
        mDb.insert(tableName, nullColumnHack, contentValues);
    }

    /**
     * Close the db relation. Make sure to do this to allow garbage collection properly.
     */
    private void closeDb() {
        mDb.close();
    }

    /**
     * Drop all shopping list lines with the "is done" flag set.
     */
    int dropCompletedShoppingLines() {
        return dropLinesFromTable(SQL_WHERE_DROP_DONE_LINES);
    }

    /**
     * Emptying of the database table for shopping list lines.
     */
    int dropAllShoppingLines() {
        return dropLinesFromTable(null);
    }

    /**
     * Wrapper for a SQL INSERT.
     *
     * @param content  The string stating what is to be bought.
     * @param category A string stating the category of the buy.
     *                 Has to be input validated beforehand.
     */
    void addShoppingLine(@NonNull String content, @NonNull String category) {
        ContentValues cv = new ContentValues();
        cv.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY, category);
        cv.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT, content);
        mDb.insert(ShoppingListContract.ShoppingListLine.TABLE_NAME, null, cv);
    }

    /**
     * A filter class which can filter the adapter data.
     */
    class LineFilterer implements FilterQueryProvider {

        /**
         * Runs a query with the specified constraint. This query is requested
         * by the filter attached to this adapter.
         * <p/>
         * Contract: when constraint is null or empty, the original results,
         * prior to any filtering, must be returned.
         *
         * @param constraint the constraint with which the query must
         *                   be filtered
         * @return a Cursor representing the results of the new query
         */
        @Override
        public Cursor runQuery(CharSequence constraint) {

            StringBuilder sb = new StringBuilder(
                    ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY + " in (");
            sb.append(constraint.subSequence(1, constraint.length()).toString());
            sb.append(") ");

            if (constraint.charAt(0) == '1') {

                sb.append("and "
                        + ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE + " == 0");
            }
            Log.d(Toolbox.LOG_TAG, "filter where clause is: " + sb.toString());
            return getLines(sb.toString());
        }
    }

    /**
     * A wrapper for the appropriate SQL SELECT getting relevant shopping list lines from the db
     *
     * @param whereClause a string that goes in the SQL clause.
     * @return a cursor holding the result set for use with the ShoppingListCursorAdapter
     */
    Cursor getLines(@Nullable String whereClause) {

        if (whereClause == null) {
            Log.d(Toolbox.LOG_TAG, "Running getLines with a null string as whereClause");
        }

        if (mDb == null) {
            throw new IllegalStateException("Tried to access lines before opening a db");
        }

        Log.v(Toolbox.LOG_TAG, "Fetching data with the where clasue" + whereClause);

        return mDb.query(
                ShoppingListContract.ShoppingListLine.TABLE_NAME,
                LINE_PROJECTION,
                whereClause,
                null,
                null,
                null,
                LINE_SORT_ORDER
        );

    }
}
