package com.example.sara.dinneraid;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public class ClearShoppingLinesFragment extends DialogFragment {

    public interface ShoppingLineClearListener {
        public void clearAll();
        public void clearDone();
    }

    // Use this instance of the interface to deliver action events
    ShoppingLineClearListener mListener;

    // Override the Fragment.onAttach() method to instantiate the Listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the listener so we can send events to the host
            mListener = (ShoppingLineClearListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ShoppingLineClearListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final CharSequence[] items = {"All items","Completed items"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int[] selectedCase = new int[1];
        selectedCase[0] = 1;

        builder.setTitle("What items to clear?")
                .setSingleChoiceItems(items, selectedCase[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        selectedCase[0] = item;
                    }
                })
                .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        switch (selectedCase[0]) {
                            case 0:
                                mListener.clearAll();
                                Log.d(Toolbox.LOG_TAG,"Clearing all items.");
                                return;
                            case 1:
                                mListener.clearDone();
                                Log.d(Toolbox.LOG_TAG,"Clearing done items.");
                                return;
                            default:
                                Log.d(Toolbox.LOG_TAG,"No button selected");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(Toolbox.LOG_TAG,"Cancelled the clear dialog.");
                    }
                });
        return builder.create();
    }

}
