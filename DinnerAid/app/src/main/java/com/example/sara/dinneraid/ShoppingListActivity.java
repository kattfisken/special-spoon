package com.example.sara.dinneraid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    ArrayList<ShoppingListLine> shoppingListArray;
    ShoppingListLineAdapter shoppingListLineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        shoppingListArray = new ArrayList<ShoppingListLine>();
        shoppingListArray.add(new ShoppingListLine("1 l mjölk", ShoppingListLine.GroceryCategory.DAIRY));
        shoppingListArray.add(new ShoppingListLine("300 g nötkött", ShoppingListLine.GroceryCategory.MEAT));
        shoppingListArray.add(new ShoppingListLine("3 röda paprikor", ShoppingListLine.GroceryCategory.VEGETABLES));
        Log.d(Constants.LOG_TAG, "initialized the satic array");

        shoppingListLineAdapter = new ShoppingListLineAdapter(
                this
                ,R.layout.list_item_shopping_line
                ,shoppingListArray);
        Log.d(Constants.LOG_TAG, "Created the array adapter");

        ListViewCompat shoppingList = (ListViewCompat) findViewById(R.id.list_shopping);
        if (shoppingList != null) {
            shoppingList.setAdapter(shoppingListLineAdapter);
        } else {
            Log.e(Constants.LOG_TAG,"Couldn't find the listView for list items. Did the inflater fail?");
        }

        Log.d(Constants.LOG_TAG, "onCreate finished");
    }

}