package com.example.sara.dinneraid;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

/**
 * The only activity in this app. A shopping list app for groceries.
 */
public class ShoppingActivity
        extends
        AppCompatActivity
        implements
        SelectCategoriesDialogFragment.GroceryCategoryFilterDialogListener
        , ClearShoppingLinesFragment.ShoppingLineClearListener
        , NewLineFragment.NewLineListener {

    private ShoppingListCursorAdapter shoppingListAdapter;
    private boolean[] filterCategories;
    private int filterDone = 0;
    private Cursor mCursor;
    private ShoppingListDbHelper dbHelper;


    /**
     * Sets up GUI, the toolbar and resets filters.
     *
     * @param savedInstanceState not used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetFilterValues();
        Log.v(Toolbox.LOG_TAG, "onCreate finished");
    }

    /**
     * Open database connections onStart.
     */
    @Override
    protected void onStart() {
        super.onStart();

        dbHelper = new ShoppingListDbHelper(this);
        mCursor = dbHelper.getLines(null);
        Log.d(Toolbox.LOG_TAG, "loaded a cursor from the db");

        shoppingListAdapter = new ShoppingListCursorAdapter(this, mCursor, 0, dbHelper);
        Log.d(Toolbox.LOG_TAG, "Created the array adapter");

        ListViewCompat shoppingList = (ListViewCompat) findViewById(R.id.list_shopping);
        if (shoppingList != null) {
            shoppingList.setAdapter(shoppingListAdapter);
        } else {
            Log.e(Toolbox.LOG_TAG, "Couldn't find the listView for list items. Did the inflater fail?");
        }
    }

    /**
     * Close database connections on app stop.
     */
    @Override
    protected void onStop() {
        mCursor.close();
        dbHelper.close();
        super.onStop();
    }

    /**
     * Inflate the menu.
     *
     * @param menu The menu to inflate.
     * @return True, to show the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        Log.d(Toolbox.LOG_TAG, "Finished making the menu");
        return true;
    }

    /**
     * Method to call when pressing buttons in the options menu.
     *
     * @param item The pressed menu item.
     * @return True.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                Log.d(Toolbox.LOG_TAG, "User wants to edit category filters");
                askForCategoryFilterDialog();
                return true;
            case R.id.menu_item_toggle_completed:
                Log.d(Toolbox.LOG_TAG, "want to toggle copmleted");
                filterDone = 1 - filterDone;


                int s_new = (filterDone == 1) ?
                        R.string.menu_item_toggle_on : R.string.menu_item_toggle_off;
                item.setTitle(s_new);

                int ic_new = (filterDone == 1) ?
                        R.drawable.ic_visibility_white_24dp : R.drawable.ic_visibility_off_white_24dp;
                item.setIcon(ic_new);

                refreshAdapterData();

                int textToShow = (filterDone == 1) ?
                        R.string.toast_hint_hide_done : R.string.toast_hint_show_done;

                Toast.makeText(this, textToShow, Toast.LENGTH_SHORT).show();

                return true;
            case R.id.menu_item_clear:
                Log.d(Toolbox.LOG_TAG, "want to clear lines");
                clearLinesDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Open a dialog for what categories to filter on.
     */
    public void askForCategoryFilterDialog() {
        DialogFragment newFragment = new SelectCategoriesDialogFragment();
        newFragment.show(getSupportFragmentManager(), "GroceryCategoryFilterDialog");
    }

    /**
     * Open a dialog about what lines to delete from the list.
     */
    public void clearLinesDialog() {
        DialogFragment newFragment = new ClearShoppingLinesFragment();
        newFragment.show(getSupportFragmentManager(), "ClearLines");
    }

    /**
     * Open a new dialog for creating a new shopping line.
     *
     * @param v not used. only for xml integration.
     */
    public void newItemDialog(@Nullable View v) {
        DialogFragment newFragment = new NewLineFragment();
        newFragment.show(getSupportFragmentManager(), "NewItemDialog");
    }


    /**
     * Add a new line to the shopping list (Database and GUI).
     *
     * @param content  The grocery in question.
     * @param category The category of the grocery.
     */
    @Override
    public void addShoppingListLine(String content, String category) {
        dbHelper.addShoppingLine(content, category);

        refreshAdapterData();

    }

    /**
     * Perform filtering based on the global variable filterDone and the passed variable
     * filterCategories.
     *
     * @param filterCategories A bool array representing whether each and every category should
     *                         be shown or not.
     */
    public void filterShoppingList(boolean[] filterCategories) {
        this.filterCategories = filterCategories;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MenuItem item = null;
        if (toolbar != null) {
            item = toolbar.getMenu().findItem(R.id.menu_item_filter);
        }
        int ic_new = (Toolbox.anyFalse(filterCategories)) ?
                R.drawable.ic_funnel_filled : R.drawable.ic_funnel_empty;
        if (item != null) {
            item.setIcon(ic_new);
        }

        refreshAdapterData();
    }

    /**
     * Getter for filterCategories
     *
     * @return What categories should be included/excluded from the filtering.
     */
    @Override
    public boolean[] getActiveFilters() {
        return filterCategories;
    }

    /**
     * Reload data from the database into the adapter.
     */
    public void refreshAdapterData() {
        String[] allCategories = getResources().getStringArray(R.array.grocery_categories);
        String[] stringArr = Toolbox.filterByBoolList(allCategories, filterCategories);
        String filterCategoriesString = '"' + Toolbox.join(stringArr, "\",\"") + '"';
        Log.d(Toolbox.LOG_TAG, "Running refreshAdapterData() with filtering string: " + filterDone + filterCategoriesString);
        shoppingListAdapter.getFilter().filter(filterDone + filterCategoriesString);
    }

    /**
     * Delete all rows in the database.
     */
    @Override
    public void clearAll() {

        int nrOfRowsDelted = dbHelper.dropAllShoppingLines();

        refreshAdapterData();

        Toast.makeText(this, nrOfRowsDelted + " row(s) deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Delete all rows from the shopping list database, where the shopping line has been ticked off.
     */
    @Override
    public void clearDone() {

        int nrOfRowsDelted = dbHelper.dropCompletedShoppingLines();

        refreshAdapterData();

        Toast.makeText(this, nrOfRowsDelted + " row(s) deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Reset the filters so that all categories are shown and all items (ticked off or not)
     * are shown.
     */
    private void resetFilterValues() {

        filterDone = 0;

        String[] tmpArr = getResources().getStringArray(R.array.grocery_categories);
        filterCategories = new boolean[tmpArr.length];
        Arrays.fill(filterCategories, Boolean.TRUE);

        Log.v(Toolbox.LOG_TAG, "reset filters");
    }
}