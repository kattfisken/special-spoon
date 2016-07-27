package com.example.sara.uppg1;


import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private final String[] nameRepo = {"Ludvig", "Kalle", "Pelle", "Nisse", "Jan", "Karl", "Carl-Gustaf", "Gustaf", "Gustav"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<String> defaultNameList = new ArrayList<String>();

        nameListArrayAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_list_item_1
                ,defaultNameList);

        setContentView(R.layout.activity_name_generator);

        ListView nameList = (ListView)findViewById(R.id.name_list);
        if (nameList != null) {
            nameList.setAdapter(nameListArrayAdapter);
        } else {
            android.util.Log.e(LOGTAG,"listView har försvunnit");
        }

        for(int i=1; i<=3;i++){
            addRandomName();
        }

        nameList.setOnItemClickListener(
                new AdapterView.OnItemClickListener()  {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3){
                        nameListArrayAdapter.remove(nameListArrayAdapter.getItem(position));
                    }
                }


        );

    }
    public void addRandomName(View v) {
        addRandomName();
    }

    public void addRandomName() {
        int rnd = new Random().nextInt(nameRepo.length);
        String randomName = nameRepo[rnd];



        //leta igenom hela listan
        //om den är i listan så ta rnd++; och börja om letandet
        // om rnd > listan är lång så skall man gå till första namnet
        // om alla namn prövats, så ge ett felmeddelande (toast?)
        int n_try = 0;
        boolean name_already_in_list = true;
        while ((n_try < nameRepo.length) && name_already_in_list) {
            randomName = nameRepo[rnd];
            n_try++;
            name_already_in_list = false; // assume it is not in the list....
            for (int try_pos=0;try_pos<nameListArrayAdapter.getCount();try_pos++) {
                // but if we find it when looking for it...
                if (randomName.equals(nameListArrayAdapter.getItem(try_pos))) {
                    //try with next name from the repo
                    rnd++;
                    rnd = rnd % nameRepo.length;
                    name_already_in_list = true;
                    break;
                }
            }
        }

        // så vid scannandet och slumpandets slut...
        if (name_already_in_list) {
            android.util.Log.e(LOGTAG,"Name already in the list.");
            Snackbar sb = Snackbar.make(
                    findViewById(R.id.root_layout)
                    ,R.string.no_more_names_snackbar
                    ,Snackbar.LENGTH_SHORT);
            sb.show();

        } else {
            nameListArrayAdapter.add(randomName);
        }


    }
}
