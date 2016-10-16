package com.example.sara.dinneraid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ShoppingListLineAdapter extends ArrayAdapter<ShoppingListLine> {

    ViewHolder viewHolder;

    private static class ViewHolder {
        private TextView isDoneView;
        private TextView contentView;
        private TextView categoryView;
    }

    public ShoppingListLineAdapter(Context context, int textViewResourceId, ArrayList<ShoppingListLine> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_item_shopping_line, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.isDoneView= (TextView) convertView.findViewById(R.id.text_is_done);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.categoryView = (TextView) convertView.findViewById(R.id.text_category);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShoppingListLine item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.contentView.setText(String.format("%s", item.getContent()));
            viewHolder.categoryView.setText(String.format("%s", item.getCategory()));
            viewHolder.isDoneView.setText(String.format("%s", item.getIsDone()));
        }

        return convertView;
    }
}
