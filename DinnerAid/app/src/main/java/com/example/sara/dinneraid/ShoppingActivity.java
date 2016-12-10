package com.example.sara.dinneraid;

import android.database.Cursor;
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

public class ShoppingActivity extends AppCompatActivity
        implements SelectGroceryCategoriesDialogFragment.GroceryCategoryFilterDialogListener, ClearShoppingLinesFragment.ShoppingLineClearListener, NewLineFragment.NewLineListener {

    private ArrayList<ShoppingListLine> shoppingListArray;
    private ShoppingListCursorAdapter shoppingListAdapter;
    private String[] filterCategories;
    private int filterDone = 0;
    private Cursor mCursor;
    private ShoppingListDbHelper dbHelper;


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



        dbHelper = new ShoppingListDbHelper(this);

        mCursor = dbHelper.getLines(null);
        Log.v(Toolbox.LOG_TAG, "loaded a cursor from the db");

        shoppingListAdapter = new ShoppingListCursorAdapter(this, mCursor,0,dbHelper);
        Log.d(Toolbox.LOG_TAG, "Created the array adapter");


        ListViewCompat shoppingList = (ListViewCompat) findViewById(R.id.list_shopping);
        if (shoppingList != null) {
            shoppingList.setAdapter(shoppingListAdapter);
        } else {
            Log.e(Toolbox.LOG_TAG,"Couldn't find the listView for list items. Did the inflater fail?");
        }

        resetFilterValues();
        Log.v(Toolbox.LOG_TAG, "onCreate finished");
    }

    @Override
    protected void onDestroy() {
        // these should rather be closed on "stopped" and created at "onStart" to save some memory
        // that would require some thinking and refactoring though...
        // https://developer.android.com/training/basics/activity-lifecycle/stopping.html
        mCursor.close();
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        Log.d(Toolbox.LOG_TAG,"Finished making the menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                Log.d(Toolbox.LOG_TAG,"want to edit filters");
                askForCategoryFilterDialog();
                return true;
            case R.id.menu_item_toggle_completed:
                Log.d(Toolbox.LOG_TAG,"want to toggle copmleted");
                filterDone = 1-filterDone;
                String s_off = getString(R.string.menu_item_toggle_off);
                String s_on = getString(R.string.menu_item_toggle_on);
                String s_old = item.getTitle().toString();
                String s_new = (s_old.equals(s_off)) ? s_on : s_off;
                item.setTitle(s_new);
                refreshAdapterData();
                return true;
            case R.id.menu_item_clear:
                Log.d(Toolbox.LOG_TAG,"want to clear lines");
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
    public void addShoppingListLine(String content, String category) {
        dbHelper.addShoppingLine(content,category);

        refreshAdapterData();

    }

    public void filterShoppingList(String[] filterCategories) {
        this.filterCategories = filterCategories;
        refreshAdapterData();
    }

    public void refreshAdapterData() {

        String filterCategoriesString = '"'+ Toolbox.join(filterCategories,"\",\"")+'"';
        Log.d(Toolbox.LOG_TAG,"Running refreshAdapterData() with filtering string: "+filterDone+filterCategoriesString);
        shoppingListAdapter.getFilter().filter(filterDone+filterCategoriesString);
    }

    @Override
    public void clearAll() {

        dbHelper.dropAllShoppingLines();

        refreshAdapterData();

    }

    @Override
    public void clearDone() {

        dbHelper.dropCompletedShoppingLines();

        refreshAdapterData();
    }

    private void resetFilterValues() {

        filterDone = 0;
        filterCategories = getResources().getStringArray(R.array.grocery_categories);
        Log.v(Toolbox.LOG_TAG,"reset filters");
    }
}