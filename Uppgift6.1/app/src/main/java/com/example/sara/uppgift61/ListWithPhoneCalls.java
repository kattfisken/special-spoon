package com.example.sara.uppgift61;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main app activity. Shows a list of calls from the call log. Whole screen is white if no calls
 * have been made (e.g. you open a new emulator). The list is refreshed onResume. Press a number in
 * the list to start a new call to that number.
 */
public class ListWithPhoneCalls extends ListActivity {


    @Override
    protected void onCreate(Bundle b) {
        Log.d(Constants.LOG_TAG, "onCreate method started");
        super.onCreate(b);
        setContentView(R.layout.activity_list_with_phone_calls);

        Log.d(Constants.LOG_TAG, "on create finished");
    }

    /**
     * <p>Open a new cursor and set it to the listView. This is done so that the list is always
     * updated.</p>
     * <p>Since I am coding for API 15, I need to use several bad practises in below code. Most
     * notably, I am reading data from the call log in the GUI thread. It's in theory bad for app
     * performance, but it is not really an issue in this very simple app.</p>
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "Starting onResume");

        try {

            //this code below is deprecated as explained by the following blog post:
            // http://www.androiddesignpatterns.com/2012/07/loaders-and-loadermanager-background.html

            // WARNING cursor is created in UI thread
            Log.d(Constants.LOG_TAG, "opening cursor");
            Cursor mCursor = this.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI //the content to query
                    , null // no projection
                    , null // no where clause
                    , null // no where clause arguments
                    , CallLog.Calls.DATE + " DESC"); // sorting

            if (mCursor != null) {

                /* Deprecated method. Should use a LoaderManager and a CursorLoader to move the
                *  cursor work away from the UI thread. The cursor life cycle is now together with
                *  the UI thread which is bad practise
                */
                Log.d(Constants.LOG_TAG, "starts managing cursor");
                //noinspection deprecation
                startManagingCursor(mCursor);

                /* the SimpleCursorAdapter is somewhat deprecated. It doesn't work well performance-
                * wise when it is handled in the fashion it is used in this program.
                * it cannot be used with steadily changing data sources. :(
                * The flag 0 passed as last argument to the constructor tells the adapter <em>not
                * </em> to listen for changes in the underlying data, since that is what would cause
                * bad performance. I think it is good enough that the reload of data happens in
                * {@ref onCreate}.
                */

                ListAdapter adapter = new HomeMadeAdapter(
                        this           //context
                        , mCursor      //Cursor
                        , 0);          //do not listen for changes in data

                setListAdapter(adapter);
            }
        } catch (SecurityException se) {
            Toast.makeText(this, R.string.toast_no_permission, Toast.LENGTH_LONG).show();
            Log.d(Constants.LOG_TAG, "No permission allowed.");
            se.printStackTrace();
        }
    }

    /**
     * Callback method for clicking on items in the ListView - i.e. numbers in the call log.
     *
     * @param l        The ListView holding the list items.
     * @param v        The View that was clicked.
     * @param position The position in the list of the View that was clicked.
     * @param id       Not in use.
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        View w = v.findViewById(android.R.id.text1);
        TextView tw = (TextView) w;
        String telephoneNumber = tw.getText().toString();
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + telephoneNumber));
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.error_msg_no_phone_app, Toast.LENGTH_LONG).show();
        }
    }
}
