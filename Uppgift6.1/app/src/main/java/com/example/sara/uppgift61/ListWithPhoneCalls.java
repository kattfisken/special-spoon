package com.example.sara.uppgift61;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListWithPhoneCalls extends ListActivity {
    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.square_pants);

    try{
        Cursor mCursor = this.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (mCursor != null) {
            startManagingCursor(mCursor);

            // deprecated constructor (though the class itself is good...)
            ListAdapter adapter = new android.support.v4.widget.SimpleCursorAdapter(
                    this,
                    android.R.layout.two_line_list_item,
                    mCursor,
                    new String[] {CallLog.Calls.NUMBER, CallLog.Calls.DATE},
                    new int[]{android.R.id.text1,android.R.id.text2 });

            setListAdapter(adapter);


        }
    }
    catch (SecurityException se){
        Toast.makeText(this, R.string.toast_no_permission,Toast.LENGTH_LONG).show();
        Log.d(Constants.LOG_TAG,"No permission allowed.");
        se.printStackTrace();
    }

        Log.d(Constants.LOG_TAG, "on create finished");
    }


}
