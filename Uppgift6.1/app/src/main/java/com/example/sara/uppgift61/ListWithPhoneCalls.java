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

public class ListWithPhoneCalls extends ListActivity {


    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.square_pants);

        try {

            //this code below is deprecated as explained by the following blog post:
            // http://www.androiddesignpatterns.com/2012/07/loaders-and-loadermanager-background.html

            // WARNING cursor is created in UI thread
            Cursor mCursor = this.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI //the content to query
                    , null // no projection
                    , null // no where clause
                    , null // no where clause arguments
                    , null); // no sorting
            if (mCursor != null) {
                //deprecated method. Should use a LoaderManager and a CursorLoader to move the cursor work from the UI thread
                //the cursor life cycle is with UI thread which is bad practise
                //noinspection deprecation
                startManagingCursor(mCursor);

                // the SimpleCursorAdapter is somewhat deprecated.
                // it cannot be used with stadily changing data sources. :(
                // thus the flag 0 has been sent
                ListAdapter adapter;
                adapter = new HomeMadeAdapter(
                        this           //context
                        , mCursor      // the Cursor
                        , 0);          //do not listen for changes in data

                setListAdapter(adapter);
            }
        } catch (SecurityException se) {
            Toast.makeText(this, R.string.toast_no_permission, Toast.LENGTH_LONG).show();
            Log.d(Constants.LOG_TAG, "No permission allowed.");
            se.printStackTrace();
        }

        Log.d(Constants.LOG_TAG, "on create finished");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        View w = v.findViewById(android.R.id.text1);
        TextView tw = (TextView)w;
        String telephoneNumber = tw.getText().toString();
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + telephoneNumber));
        if (i.resolveActivity(getPackageManager())!= null){
            startActivity(i);
        }else{
            Toast.makeText(this, R.string.error_msg_no_phone_app,Toast.LENGTH_LONG).show();
        }
    }
}
