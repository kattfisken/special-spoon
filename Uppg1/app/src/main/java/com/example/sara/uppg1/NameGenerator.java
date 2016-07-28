package com.example.sara.uppg1;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator extends AppCompatActivity {

    public ArrayAdapter<String> nameListArrayAdapter;
    private final String LOGTAG = "Uppgift 1 Custom log";
    private String[] static_array_of_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        static_array_of_names = getResources().getStringArray(R.array.long_list_of_names);


        ArrayList<String> defaultNameList = new ArrayList<String>();

        nameListArrayAdapter = new ArrayAdapter<String>(this
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

    class NameDeleteListener implements AdapterView.OnItemClickListener {

        private ArrayAdapter<String> arrayAdapter;


        public NameDeleteListener(ArrayAdapter<String> input_aa) {
            arrayAdapter = input_aa;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



            AlertDialog.Builder builder =
                    new AlertDialog.Builder(NameGenerator.this);
            builder.setCancelable(true);
            builder.setMessage(R.string.delete_dialog_message);
            builder.setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                int position_to_delete = position;
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                }
            });
            builder.setNegativeButton(R.string.delete_dialog_negative, null);
            builder.show();

        }
    }


    public void addRandomName(View v) {
        addRandomName();
    }

    public void addRandomName() {
        int my_random_int;
        String randomName;
        int n_try;
        boolean name_already_in_list;
        View root_view;

        my_random_int = new Random().nextInt(static_array_of_names.length);
        randomName = static_array_of_names[my_random_int];
        n_try = 0;

        name_already_in_list = true;
        while ((n_try < static_array_of_names.length) && name_already_in_list) {


            n_try++; // for try number x....
            randomName = static_array_of_names[my_random_int]; // pick a new name to try
            name_already_in_list = false; // and this name, have not yet been found in the list of displayed names


            for (int try_pos=0;try_pos<nameListArrayAdapter.getCount();try_pos++) {

                // but if we find it when looking for it...
                if (randomName.equals(nameListArrayAdapter.getItem(try_pos))) {
                    //try with next name from the repo
                    my_random_int++;
                    my_random_int = my_random_int % static_array_of_names.length;
                    name_already_in_list = true;
                    break;
                }
            }
        }

        // så vid scannandet och slumpandets slut...
        if (name_already_in_list) {
            android.util.Log.e(LOGTAG,"Name already in the list.");
            Snackbar out_of_names_error_snackbar;
            root_view = findViewById(R.id.root_layout);
            if ( root_view != null) {
                out_of_names_error_snackbar = Snackbar.make(
                        root_view
                        , R.string.no_more_names_snackbar
                        , Snackbar.LENGTH_SHORT);
                out_of_names_error_snackbar.show();
            }else {
                android.util.Log.e(LOGTAG,"The root view cannot be obtained");
            }

        } else {
            nameListArrayAdapter.add(randomName);
        }


    }
}
