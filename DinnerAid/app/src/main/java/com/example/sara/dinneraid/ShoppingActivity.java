package com.example.sara.dinneraid;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity
        implements SelectGroceryCategoriesDialogFragment.GroceryCategoryFilterDialogListener, ClearShoppingLinesFragment.ShoppingLineClearListener, NewLineFragment.NewLineListener {

    private ArrayList<ShoppingListLine> shoppingListArray;
    private ShoppingListLineAdapter shoppingListLineAdapter;
    private String filterCategories; //todo refactor so that the filtered categories are held as an array, not as a string
    private int filterDone = 0;
    private SQLiteDatabase db;


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
                newItemDialog();
            }
        });


        String[] s1 = {"1 l mjölk", "300g nötkött","3 röda paprikor","5 gurkor"};
        String[] s2 = {"Diary", "Meat","Vegetables","Vegetables"};

        ShoppingListDbHelper dbHelper = new ShoppingListDbHelper(this);
        db = dbHelper.getWritableDatabase();
        Log.d(Constants.LOG_TAG, "opened the db");
        for (int j = 0; j< s1.length;j++) {
            ContentValues  contentValues = new ContentValues();
            contentValues.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT,s1[j]);
            contentValues.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY,s2[j]);
            db.insert(ShoppingListContract.ShoppingListLine.TABLE_NAME,null, contentValues);
        }
        Log.v(Constants.LOG_TAG, "loaded the db with dummy records");


        db = dbHelper.getReadableDatabase();
        String[] projection = {
                ShoppingListContract.ShoppingListLine._ID,
                ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT,
                ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY,
                ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE
        };
        String sortOrder =
                ShoppingListContract.ShoppingListLine._ID + " DESC";
        Cursor c = db.query(
                ShoppingListContract.ShoppingListLine.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Log.v(Constants.LOG_TAG, "loaded a cursor from the db");

        c.moveToFirst();
        shoppingListArray = new ArrayList<ShoppingListLine>();
        while(!c.isAfterLast()) {
            String category = c.getString(c.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CATEGORY));
            String content = c.getString(c.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_CONTENT));
            int isdoneInt = c.getInt(c.getColumnIndex(ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE));
            boolean isdone = isdoneInt != 0;

            shoppingListArray.add(new ShoppingListLine(content, new GroceryCategory(this, category),isdone));
            c.moveToNext();
        }
        c.close();

        Log.d(Constants.LOG_TAG, "converted cursor to array");

        //todo dont convert the cursor to an array - instead let the adapter work on the cursor!1


        shoppingListLineAdapter = new ShoppingListLineAdapter(
                this
                ,R.layout.list_item_shopping_line
                ,shoppingListArray
        );
        Log.d(Constants.LOG_TAG, "Created the array adapter");


        ListViewCompat shoppingList = (ListViewCompat) findViewById(R.id.list_shopping);
        if (shoppingList != null) {
            shoppingList.setAdapter(shoppingListLineAdapter);
        } else {
            Log.e(Constants.LOG_TAG,"Couldn't find the listView for list items. Did the inflater fail?");
        }

        resetFilterValues();
        Log.d(Constants.LOG_TAG, "onCreate finished");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        Log.d(Constants.LOG_TAG,"Finished making the menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                Log.d(Constants.LOG_TAG,"want to edit filters");
                askForCategoryFilterDialog();
                return true;
            case R.id.menu_item_toggle_completed:
                Log.d(Constants.LOG_TAG,"want to toggle copmleted");
                filterDone = 1-filterDone;
                filterAdapter();
                return true;
            case R.id.menu_item_clear:
                Log.d(Constants.LOG_TAG,"want to clear lines");
                clearLinesDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void askForCategoryFilterDialog() {
        DialogFragment newFragment = new SelectGroceryCategoriesDialogFragment();
        newFragment.show(getSupportFragmentManager(), "GroceryCategoryFilterDialog");
    }

    public void clearLinesDialog() {
        DialogFragment newFragment = new ClearShoppingLinesFragment();
        newFragment.show(getSupportFragmentManager(), "ClearLines");
    }

    private void newItemDialog() {
        DialogFragment newFragment = new NewLineFragment();
        newFragment.show(getSupportFragmentManager(), "NewItemDialog");
    }

    @Override
    public void addShoppingListLine(ShoppingListLine item) {
        shoppingListLineAdapter.add(item);
    }

    @Override
    public void filterShoppingList(String filterString) {
        filterCategories = filterString;
        filterAdapter();
    }

    public void filterAdapter() {
        Log.d(Constants.LOG_TAG,"now we want to filter! String:"+filterDone+filterCategories);
        shoppingListLineAdapter.getFilter().filter(filterDone+filterCategories);
    }

    @Override
    public void clearAll() {
        shoppingListLineAdapter.clear();
    }

    @Override
    public void clearDone() {
        shoppingListLineAdapter.clearDone();
        shoppingListLineAdapter.getFilter().filter(filterDone+filterCategories);
    }

    private void resetFilterValues() {

        // todo remake so filtering is not held as a string, only is passed as a string
        filterDone = 0;
        List<String> allowedCategories = Arrays.asList(getResources().getStringArray(R.array.grocery_categories));
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: allowedCategories) {
            sb.append(sep).append(s);
            sep = ",";
        }
        filterCategories = sb.toString();
    }
}