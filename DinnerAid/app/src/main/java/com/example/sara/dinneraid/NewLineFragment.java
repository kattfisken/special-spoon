package com.example.sara.dinneraid;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

public class NewLineFragment extends DialogFragment {


    public interface NewLineListener {
        public void addShoppingListLine(ShoppingListLine item);
    }

    // Use this instance of the interface to deliver action events
    NewLineListener mListener;

    // Override the Fragment.onAttach() method to instantiate the Listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the listener so we can send events to the host
            mListener = (NewLineListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewLineListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Specify new shopping item");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_new_line, null);

        // Pass null as the parent view because its going in the dialog layout


        AppCompatSpinner spinner = (AppCompatSpinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.grocery_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(v);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String content = ((AppCompatEditText) v.findViewById(R.id.new_content)).getText().toString();
                String category = ((AppCompatSpinner) v.findViewById(R.id.category_spinner)).getSelectedItem().toString();
                ShoppingActivity activity = ((ShoppingActivity) getActivity());
                mListener.addShoppingListLine(new ShoppingListLine(content, new GroceryCategory(activity, category)));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                NewLineFragment.this.getDialog().cancel();
            }
        });


        return builder.create();
    }

}
