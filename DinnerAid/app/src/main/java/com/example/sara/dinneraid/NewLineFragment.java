package com.example.sara.dinneraid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * A fragment that asks the user for input to make a new line in the shopping list.
 */
public class NewLineFragment extends DialogFragment {


    /**
     * An interface that anyone that calls this Fragment must implement.
     */
    public interface NewLineListener {
        void addShoppingListLine(String content, String category);
    }

    // Use this instance of the interface to deliver action events.
    NewLineListener mListener;

    /**
     * The onAttach makes sure that the activity creating the dialog has implemented the necessary
     * callback methods.
     *
     * @param activity The activity this dialog is linked to.
     */
    @Override
    public void onAttach(Activity activity) throws ClassCastException {
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

    /**
     * The method creating the actual dialog.
     *
     * @param savedInstanceState not in use.
     * @return The Dialog.
     */

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.fragment_new_line_title));

        LayoutInflater inflater = getActivity().getLayoutInflater();

        /*I dont want to hold a reference to parent. It would make the code too complex. Thus, I
        * pass null as parent to the inflater */
        @SuppressLint("InflateParams")
        final View v = inflater.inflate(R.layout.fragment_new_line, null);

        // Pass null as the parent view because its going in the dialog layout


        final NoDefaultSpinner spinner = (NoDefaultSpinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.grocery_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(v);

        builder.setPositiveButton(getString(R.string.fragment_new_line_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String content = ((AppCompatEditText) v.findViewById(R.id.new_content)).getText().toString();
                if (-1 == spinner.getSelectedItemPosition()) {
                    Toast.makeText(getContext(), R.string.toast_hint_no_content, Toast.LENGTH_SHORT).show();
                } else if (content.length() == 0) {
                    Toast.makeText(getContext(), R.string.toast_hint_no_item, Toast.LENGTH_SHORT).show();
                } else {
                    String category = spinner.getSelectedItem().toString();
                    mListener.addShoppingListLine(content, category);
                }
            }
        });

        builder.setNegativeButton(getString(R.string.fragment_new_line_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                NewLineFragment.this.getDialog().cancel();
            }
        });


        return builder.create();
    }

    /**
     * Implementation of a bug-hack. The keyboard dont show on my old Samsung Galaxy S2 when the
     * dialog opens, so this little hack forces a display of the keyboard.
     *
     * @param savedInstanceState not used.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window w = getDialog().getWindow();
        if (w != null) {
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        super.onActivityCreated(savedInstanceState);
    }
}
