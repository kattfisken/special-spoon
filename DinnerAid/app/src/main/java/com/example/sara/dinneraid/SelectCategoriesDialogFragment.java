package com.example.sara.dinneraid;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Arrays;

/**
 * A dialog fragment class that shows a dialog for selecting what categories to filter on.
 */
public class SelectCategoriesDialogFragment extends DialogFragment {

    /**
     * A interface that all users of this fragment must implement.
     */
    public interface GroceryCategoryFilterDialogListener {
        void filterShoppingList(boolean[] filterCategories);

        boolean[] getActiveFilters();
    }

    // Use this instance of the interface to deliver action events
    GroceryCategoryFilterDialogListener mListener;

    /**
     * Makes sure that the calling activity has implemented the neccessary interface.
     *
     * @param activity Calling activity.
     */
    @Override
    public void onAttach(Activity activity) throws ClassCastException {
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

    /**
     * Create the actual dialog.
     *
     * @param savedInstanceState not used.
     * @return the dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final boolean[] mSelectedItems = mListener.getActiveFilters();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.fragment_select_categories_title))
                .setMultiChoiceItems(
                        R.array.grocery_categories
                        , mSelectedItems
                        , new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                mSelectedItems[which] = isChecked;
                            }
                        })
                .setPositiveButton(getString(R.string.fragment_select_categories_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(Toolbox.LOG_TAG, "want to apply the filter");
                        Log.d(Toolbox.LOG_TAG, "selection:" + Arrays.toString(mSelectedItems));

                        mListener.filterShoppingList(mSelectedItems);

                    }
                })
                .setNegativeButton(getString(R.string.fragment_select_categories_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(Toolbox.LOG_TAG, "Cancelled the filter dialog.");
                    }
                });

        return builder.create();
    }
}
