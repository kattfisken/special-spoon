package com.example.sara.dinneraid;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
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
    }

    public void setData(ShoppingListLine inData) {
        this.data = inData;
    }

    public ShoppingListLine getData() {
        return this.data;
    }

    public void render() {

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

    public void setCheckBoxListener(View.OnClickListener listener){
        isDone.setOnClickListener(listener);
    }

}
