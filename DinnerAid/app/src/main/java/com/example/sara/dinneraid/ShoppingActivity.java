package com.example.sara.dinneraid;

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

    ArrayList<ShoppingListLine> shoppingListArray;
    ShoppingListLineAdapter shoppingListLineAdapter;
    String filterCategories;
    int filterDone = 0;

    @Override
    public void onOkay(String filterString) {
        filterCategories = filterString;
        filterAdapter();
    }

    public void filterAdapter() {
        Log.d(Constants.LOG_TAG,"now we want to filter! String:"+filterDone+filterCategories);
        shoppingListLineAdapter.getFilter().filter(filterDone+filterCategories);
    }

    @Override
    public void onCancel() {
        Log.d(Constants.LOG_TAG,"All is fine - now filtering to do. :)");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shoppingListArray = new ArrayList<ShoppingListLine>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newItemDialog();
            }
        });


        shoppingListArray.add(new ShoppingListLine("1 l mjölk", new GroceryCategory(this, "Diary")));
        shoppingListArray.add(new ShoppingListLine("300 g nötkött", new GroceryCategory(this, "Meat")));
        shoppingListArray.add(new ShoppingListLine("3 röda paprikor", new GroceryCategory(this, "Vegetables")));
        shoppingListArray.add(new ShoppingListLine("5 gurkor", new GroceryCategory(this, "Vegetables"),true));
        Log.d(Constants.LOG_TAG, "initialized the static array");

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

    private void newItemDialog() {
        DialogFragment newFragment = new NewLineFragment();
        newFragment.show(getSupportFragmentManager(), "NewItemDialog");
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

    @Override
    public void addShoppingListLine(ShoppingListLine item) {
        shoppingListLineAdapter.add(item);
    }
}