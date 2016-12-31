package com.example.sara.uppgift721;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A class for showing a fragment with a button displaying a dialog.
 */
public class MyDialogFragment extends Fragment {

    //fragment argument corresponding to the number this section has
    private static final String ARG_SECTION_NUMBER = "section_number";
    //a name for the section
    public static String NAME = "Dialog";

    // input data for the Dialog Again function
    static final CharSequence[] colorArr = new CharSequence[]{"Blue", "Green", "Red"};
    // color ints corresponding to CharSequence array above. hex based AARGGBB tuplet
    static final int[] colorValArr = new int[]{0xFF0000FF, 0xFF00FF00, 0xFFFF0000};
    // app background color initialized to default
    int color;


    /**
     * These fragments should be instantiated via factory method, not via this constructor!
     */
    public MyDialogFragment() {
    }

    /**
     * Factory method for making new fragments. Class not really utilized, but this design pattern
     * is good practise since it enables further extension.
     *
     * @param sectionNumber The number this section has in the activity.
     * @return a newly created fragment
     */
    public static MyDialogFragment newInstance(int sectionNumber) {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new view for the fragment.
     *
     * @param inflater           Inflater to use when inflating new views.
     * @param container          The viewGroup in which the Fragment sits.
     * @param savedInstanceState Old bundle for previous instances of the fragment.
     * @return An inflated View object showing the Fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        final Button button = (Button) rootView.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });


        return rootView;
    }

    /**
     * Create a dialog asking for more dialogs.
     */
    public void createDialog() {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("MORE!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createDialogAgain();
                    }
                })
                .setNegativeButton("No thanks!", null)
                .setMessage("This is a standard positive/negative AlertDialog. " +
                        "Do you want another dialog?")
                .show();
    }

    /**
     * Creates a Single choice dialog where you can choose a background color.
     */
    public void createDialogAgain() {
        int defaultColorChoice = 1;
        color = colorValArr[defaultColorChoice];
        new AlertDialog.Builder(getContext())
                .setTitle("Select a color you like.")
                .setSingleChoiceItems(colorArr, defaultColorChoice
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                color = colorValArr[which];
                            }
                        })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View v = getView();
                        if (v != null) {
                            v.setBackgroundColor(color);
                        } else {
                            Log.e(MainActivity.LOGTAG
                                    , "Couldn't set the background. View is Null!");
                        }
                    }
                })
                .show();


    }
}
