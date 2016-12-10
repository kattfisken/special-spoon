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
            public void filterShoppingList(String[] filterCategories);
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

            final ArrayList<String> mSelectedItems = new ArrayList<String>();
            final String[] allCats = getResources().getStringArray(R.array.grocery_categories);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("A title")
                    .setMultiChoiceItems(R.array.grocery_categories, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        String catToAdd = allCats[which];
                                        mSelectedItems.add(catToAdd);
                                    } else if (mSelectedItems.contains(allCats[which])) {
                                        mSelectedItems.remove(allCats[which]);
                                    }
                                }
                            })
                    .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            Log.d(Toolbox.LOG_TAG,"want to apply the filter");
                            Log.d(Toolbox.LOG_TAG,"selection:"+mSelectedItems);

                            String[] selectedCats = Toolbox.safeCast(mSelectedItems);

                            mListener.filterShoppingList(selectedCats);
                            // end to-do
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(Toolbox.LOG_TAG,"Cancelled the filter dialog.");
                        }
                    });

            return builder.create();
        }
    }
