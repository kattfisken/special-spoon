package com.example.sara.uppg1;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main class for the Name generator application.
 * It is subclassed from AppCompatActivity since my Android phone is very old and I want backwards
 * compatibility.
 */
public class NameGenerator extends AppCompatActivity {

    public ArrayAdapter<String> nameListArrayAdapter;
    private final String LOGTAG = "NameGenerator";
    private String[] staticArrayOfNames;

    /**
     * The onCreate method is ran when the app (and thus the default activity, which is this class)
     * is ran.
     * @param savedInstanceState Not used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticArrayOfNames = getResources().getStringArray(R.array.long_list_of_names);

        ArrayList<String> defaultNameList = new ArrayList<>();

        nameListArrayAdapter = new ArrayAdapter<>(this
                ,android.R.layout.simple_list_item_1
                ,defaultNameList);

        setContentView(R.layout.activity_name_generator);

        ListView nameList = (ListView)findViewById(R.id.name_list);
        if (nameList != null) {
            nameList.setAdapter(nameListArrayAdapter);
            nameList.setOnItemClickListener(new NameDeleteListener(nameListArrayAdapter));
        } else {
            android.util.Log.e(LOGTAG,"the listView has disappeared");
        }

        for(int i=1; i<=3;i++){
            addRandomName();
        }
    }

    /**
     * Callback function for when pressing on the names in the name list.
     */
    class NameDeleteListener implements AdapterView.OnItemClickListener {

        private ArrayAdapter<String> arrayAdapter;

        /**
         *  Default constructor for the name on click listener (that deletes a name from the list)
         *  It holds the array adapter as a member to simplify deletion of data.
         * @param input_aa The array adapter which holds the connection between GUI and
         *                 underlying data.
         */
        NameDeleteListener(ArrayAdapter<String> input_aa) {
            arrayAdapter = input_aa;
        }

        /**
         * The listener callback function. IS ran when a name is clicked. Creates a dialog which
         * asks if we really want to delete a ceratin name. If we want to, the remove method on the
         * array adapter is called with suitable index.
         * @param parent not used. only for compatibility reasons
         * @param view not used. only for compatibility reasons
         * @param position the number of the name in the list which was clicked ( indexed by 0).
         * @param id not used. only for compatibility reasons
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(NameGenerator.this);
            builder.setCancelable(true);
            builder.setMessage(R.string.delete_dialog_message);
            builder.setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                }
            });
            builder.setNegativeButton(R.string.delete_dialog_negative, null);
            builder.show();

            Log.d(LOGTAG,"The name which was clicked has number "+position+" in the list");

        }
    }


    /**
     * Since the implementation of addRandomName dont take any arguments, and the floating action
     * button with the plus sign needs a View parameter in the function signature, this is a facade.
     * @param v The view that was pressed when this fucntion is used as a listener in the layout xml
     */
    public void addRandomName(View v) {
        addRandomName();
    }

    /**
     * Add a name from the array of names to the ArrayList which is used by the ArrayAdapter.
     *
     */
    public void addRandomName() {

        Integer randomInt = new Random().nextInt(staticArrayOfNames.length);
        String randomName = staticArrayOfNames[randomInt];
        boolean nameIsInListView = true; // set to True to make sure we enter the correct control loop
        int n_try = 0;

        Log.v(LOGTAG,"Attempting to add a name...");

        while ((n_try < staticArrayOfNames.length) && nameIsInListView) {
            n_try++; // for try number x....
            randomName = staticArrayOfNames[randomInt]; // pick a new name to try
            nameIsInListView = false; // and this name, have not yet been found in the list of displayed names


            for (int try_pos=0;try_pos<nameListArrayAdapter.getCount();try_pos++) {

                // but if we find it when looking for it...
                if (randomName.equals(nameListArrayAdapter.getItem(try_pos))) {
                    //try with next name from the repo
                    randomInt++;
                    randomInt = randomInt % staticArrayOfNames.length;
                    nameIsInListView = true;
                    break;
                }
            }
        }

        /* below is executed when either all names in Array has been tried and the last was found
        * in the listView, or when a new name was found. The two cases are separated by the variable
        *  nameIsInListView
        */
        if (nameIsInListView) {
            Log.d(LOGTAG,"No new names to add in the listView. All names have been taken.");
            Snackbar out_of_names_error_snackbar;
            View root_view = findViewById(R.id.root_layout);
            if ( root_view != null) {
                out_of_names_error_snackbar = Snackbar.make(
                        root_view
                        , R.string.no_more_names_snackbar
                        , Snackbar.LENGTH_SHORT);
                out_of_names_error_snackbar.show();
            }else {
                Log.e(LOGTAG,"The root view cannot be obtained");
            }

        } else {
            nameListArrayAdapter.add(randomName);
            Log.v(LOGTAG,"Added the name "+randomName);
        }


    }
}
