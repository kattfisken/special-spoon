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
public class ShoppingListLineView extends RelativeLayout{

    private TextView content;
    private TextView category;
    private AppCompatCheckBox isDone;
    private ShoppingListLine data;


    ShoppingListLineView(Context context) {
        super(context);
        init();
    }

    ShoppingListLineView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init();
    }

    ShoppingListLineView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context,attributeSet,defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.list_item_shopping_line, this);
        this.content = (TextView)findViewById(R.id.text_content);
        this.category = (TextView)findViewById(R.id.text_category);
        this.isDone= (AppCompatCheckBox) findViewById(R.id.checkbox_is_done);

        isDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the underlying data
                SQLiteDatabase db = (new ShoppingListDbHelper(getContext())).getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(ShoppingListContract.ShoppingListLine.COLUMN_NAME_ISDONE,!data.getIsDone());
                db.update(ShoppingListContract.ShoppingListLine.TABLE_NAME,cv,ShoppingListContract.ShoppingListLine._ID+"="+data.getId(),null);
                db.close();

                // update the the data held by the view
                data.toggleDone();

                //update the view
                render();

                // N.B. the list is not refiltered and the cursor is not re-updated!
                // no need to re-load all data from db ^_^
                // sadly we need to open/close link to database every single time.
                //possible to-do: refactor the database-write to the main activity or helper class

                Log.v(Toolbox.LOG_TAG, "Checked/unckecked a box");
            }
        });
    }

    /**
     * Setting the data object on the view and rendering the view
     * @param inData The data object to associate with the view.
     */
    public void setData(ShoppingListLine inData) {
        setData(inData,true);
    }

    /**
     * Setting the data object on the view and optionally rendering
     * @param inData The data object to associate with the view.
     * @param render set to true to render the view, else set false
     */
    public void setData(@NonNull ShoppingListLine inData, boolean render) {
        this.data = inData;
        if (render) {
            render();
        }
    }

    public ShoppingListLine getData() {
        return this.data;
    }

    public void render() {

        if (data == null) {
            Log.e(Toolbox.LOG_TAG,"No associated with the view. Cannot render! ");
            return;
        }

        category.setText(data.getCategory().toString());
        content.setText(data.getContent());

        isDone.setChecked(data.getIsDone());

        if (this.data.getIsDone()) {
            category.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
            category.setTypeface(null, Typeface.BOLD_ITALIC);
            content.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
            content.setTypeface(null, Typeface.ITALIC);
        } else {
            category.setTextColor(data.getCategory().getColor());
            category.setTypeface(null,Typeface.BOLD);
            content.setTextColor(ContextCompat.getColor(getContext(),android.R.color.primary_text_light));
            content.setTypeface(null,Typeface.NORMAL);
        }

    }


}
