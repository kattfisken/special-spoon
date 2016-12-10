package com.example.sara.dinneraid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ShoppingListCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ShoppingListDbHelper mDbHelper;

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public ShoppingListCursorAdapter(Context context, Cursor c, int flags, ShoppingListDbHelper dbHelper) {
        super(context, c, flags);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDbHelper = dbHelper;

        setFilterQueryProvider(dbHelper.new LineFilterer());

    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ShoppingListLineView(context);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ShoppingListLineView v = (ShoppingListLineView) view;
        String category = cursor.getString(cursor.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY));
        String content = cursor.getString(cursor.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT));
        int isdoneInt = cursor.getInt(cursor.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE));
        int id = cursor.getInt(cursor.getColumnIndex(ShoppingListContract.ShoppingListLine._ID));
        boolean isdone = isdoneInt != 0;

        ShoppingListLine data = new ShoppingListLine(content, new GroceryCategory(mContext, category),isdone,id);
        v.setData(data);

    }

}
