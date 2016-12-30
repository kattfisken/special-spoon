package com.example.sara.uppgift61;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

/**
 * A cursor adapter with a overridden bindView method.
 */
class HomeMadeAdapter extends CursorAdapter {


    /**
     * Call to super.
     *
     * @param context See superclass documentation.
     * @param cursor  See superclass documentation.
     * @param flags   See superclass documentation.
     */
    HomeMadeAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
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
    @SuppressLint("InflateParams") //not relevant in this situation
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.my_two_line_list_item, null);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView numberTextView = (TextView) view.findViewById(android.R.id.text1);
        TextView dateTextView = (TextView) view.findViewById(android.R.id.text2);

        //number
        int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        String numberText = cursor.getString(numberColumn);
        numberTextView.setText(numberText);

        //date
        int dateColumn = cursor.getColumnIndex(CallLog.Calls.DATE);
        String dateText = cursor.getString(dateColumn);
        Date callDate = new Date(Long.valueOf(dateText));
        String formattedDateText = callDate.toString();
        dateTextView.setText(formattedDateText);

    }
}
