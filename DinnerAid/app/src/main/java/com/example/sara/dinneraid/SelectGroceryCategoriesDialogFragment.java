package com.example.sara.dinneraid;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;


public class SelectGroceryCategoriesDialogFragment extends DialogFragment {

        public interface GroceryCategoryFilterDialogListener {
            public void onOkay(String filterString);
            public void onCancel();
        }

    // Use this instance of the interface to deliver action events
    GroceryCategoryFilterDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the Listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the listener so we can send events to the host
            mListener = (GroceryCategoryFilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GroceryCategoryFilterDialogListener");
        }
    }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
            final String[] allCats = getResources().getStringArray(R.array.grocery_categories);

            // Actually using persistent multiselections works so-so...
            //final boolean[] defaultChecked = new boolean[allCats.length];
            //for(int j = 0;j<defaultChecked.length;j++) {
            //    defaultChecked[j] = true;
            //}

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("A title")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(R.array.grocery_categories, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                    .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            Log.d(Constants.LOG_TAG,"want to apply the filter");
                            Log.d(Constants.LOG_TAG,"selection:"+mSelectedItems);
                            StringBuilder sb = new StringBuilder();
                            for(int j = 0; j < allCats.length;j++) {
                                if(mSelectedItems.contains(j)) {
                                    if(sb.length() >0) {
                                        sb.append(",");
                                    }
                                    sb.append(allCats[j]);
                                }
                            }
                            mListener.onOkay(sb.toString());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(Constants.LOG_TAG,"Cancelled the filter dialog.");
                        }
                    });

            return builder.create();
        }
    }
