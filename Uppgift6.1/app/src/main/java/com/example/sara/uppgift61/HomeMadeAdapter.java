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

public class HomeMadeAdapter extends CursorAdapter {


    public HomeMadeAdapter (Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
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
        LayoutInflater homeMadeInflater;
        homeMadeInflater = LayoutInflater.from(context);
        return homeMadeInflater.inflate(R.layout.my_two_line_list_item,null);
        //return homeMadeInflater.inflate(R.layout.my_two_line_list_item,null);
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
        TextView numberTextView;
        TextView dateTextView;

        numberTextView = (TextView)view.findViewById(android.R.id.text1);
        dateTextView = (TextView)view.findViewById(android.R.id.text2);

        String numberText;
        String dateText;

        int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int dateColumn = cursor.getColumnIndex(CallLog.Calls.DATE);
        numberText = cursor.getString(numberColumn);
        dateText = cursor.getString(dateColumn);

        //här blir det från sekunder till ett snyggt datum
        Date callDate;
        callDate = new Date(Long.valueOf(dateText));

        String formattedDateText = callDate.toString();


        numberTextView.setText(numberText);
        dateTextView.setText(formattedDateText);

    }
}
