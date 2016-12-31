package com.example.sara.dinneraid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class for populating the ListView
 * Methods much inspired by http://trickyandroid.com/protip-inflating-layout-for-your-custom-view/
 */
public class ShoppingListLineView extends RelativeLayout {

    private TextView content;
    private TextView category;
    private AppCompatCheckBox checkBox;
    private ShoppingListLine data;
    private ShoppingActivity activity;


    /**
     * Normal constructor.
     *
     * @param context the context in which all is creted.
     */
    ShoppingListLineView(Context context) {
        super(context);
        init();
    }

    /**
     * Helper method to move logic from the constructor. Very useful if there are several
     * constructors, and this pattern is used for code flexibility.
     * <p>
     * Most importantly, it sets listeners for clicks
     */
    private void init() {
        inflate(getContext(), R.layout.list_item_shopping_line, this);
        this.content = (TextView) findViewById(R.id.text_content);
        this.category = (TextView) findViewById(R.id.text_category);
        this.checkBox = (AppCompatCheckBox) findViewById(R.id.checkbox_is_done);


        try {
            // Instantiate the listener so we can send events to the host
            activity = (ShoppingActivity) getContext();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.e(Toolbox.LOG_TAG
                    , "ShoppingListLine used in a strange way. Have you refactored badly?");
        }

        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the underlying data
                SQLiteDatabase db = (new ShoppingListDbHelper(getContext())).getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE, !data.getIsBought());
                db.update(
                        ShoppingListContract.ShoppingListLine.TABLE_NAME
                        , cv, ShoppingListContract.ShoppingListLine._ID + "=" + data.getId()
                        , null);
                db.close();

                // update the the data held by the view
                data.toggleDone();

                //update the view
                render();

                if (activity != null)
                    activity.refreshAdapterData();

                Log.v(Toolbox.LOG_TAG, "Checked/unckecked a box");
            }
        });
    }

    /**
     * Setting the data object on the view and rendering the view
     *
     * @param inData The data object to associate with the view.
     */
    public void setData(ShoppingListLine inData) {
        setData(inData, true);
    }

    /**
     * Setting the data object on the view and optionally rendering
     *
     * @param inData The data object to associate with the view.
     * @param render set to true to render the view, else set false
     */
    public void setData(@NonNull ShoppingListLine inData, boolean render) {
        this.data = inData;
        if (render)
            render();
    }

    /**
     * Set the correct text values, colors, checkbox values and fonts for the View.
     */
    public void render() {

        if (data == null) {
            Log.e(Toolbox.LOG_TAG, "No associated with the view. Cannot render! ");
            return;
        }

        category.setText(data.getCategory().toString());
        content.setText(data.getContent());

        checkBox.setChecked(data.getIsBought());

        if (this.data.getIsBought()) {
            category.setTextColor(ContextCompat.getColor(getContext(), R.color.color_category_done));
            category.setTypeface(null, Typeface.BOLD_ITALIC);
            content.setTextColor(ContextCompat.getColor(getContext(), R.color.color_category_done));
            content.setTypeface(null, Typeface.ITALIC);
        } else {
            category.setTextColor(data.getCategory().getColor());
            category.setTypeface(null, Typeface.BOLD);
            content.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
            content.setTypeface(null, Typeface.NORMAL);
        }

    }


}
