package com.example.sara.dinneraid;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ShoppingListLineAdapter extends ArrayAdapter<ShoppingListLine> implements Filterable {

    ViewHolder viewHolder;
    ArrayList<ShoppingListLine> filteredData;
    ArrayList<ShoppingListLine> originalData;

    /**
     * removes all completed shopping list lines from the underlying data set.
     * does not update the filtered view - only the underlying data model!
     * make sure to update the view after calling this method!
     */
    public void clearDone() {
        int j = 0;
        while (j<originalData.size()) {
            if (originalData.get(j).getIsDone()) {
                Log.d(Constants.LOG_TAG,"removing item: "+originalData.get(j));
                originalData.remove(j);
            } else {
                j++;
            }
        }
    }

    /**
     * A ViewHolder is used to reduce the number of calls to getviewbyid.
     * It is apparently a good practice...
     */
    private static class ViewHolder {
        private CheckBox isDoneView;
        private TextView contentView;
        private TextView categoryView;
    }

    public int getCount() {
        return filteredData.size();
    }

    public ShoppingListLine getItem(int i){
        return filteredData.get(i);
    }

    public long getItemId(int position) {
        return position;
    }

    public ShoppingListLineAdapter(Context context, int textViewResourceId, ArrayList<ShoppingListLine> items) {
        super(context, textViewResourceId, items);
        filteredData = items;
        originalData = items;
    }

    /**
     * generating a line in the ListView
     * @param position the number of the item to create
     * @param convertView the "base" view that we create the final line from
     * @param parent the listview that we put the lines into
     * @return a line in the listview, asa view object
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * if we dont have a convertview accessible, we create it and put it in the viewholder for future acecess
         * else we access the previously stored convertview.
         * this is a trick for cpu efficiacy
          */
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_item_shopping_line, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.isDoneView= (CheckBox) convertView.findViewById(R.id.checkbox_is_done);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.categoryView = (TextView) convertView.findViewById(R.id.text_category);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * populate the view!
         */
        final ShoppingListLine item = getItem(position);
        if (item!= null) {
            viewHolder.contentView.setText(String.format("%s", item.getContent()));
            viewHolder.categoryView.setText(String.format("%s", item.getCategory()));

            if(item.getIsDone()) {
                viewHolder.isDoneView.setChecked(item.getIsDone());

                viewHolder.categoryView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
                viewHolder.categoryView.setTypeface(null, Typeface.BOLD_ITALIC);

                viewHolder.contentView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
                viewHolder.contentView.setTypeface(null, Typeface.ITALIC);

            } else {

                viewHolder.isDoneView.setChecked(item.getIsDone());
                viewHolder.categoryView.setTextColor(item.getCategory().getColor());

            }

            viewHolder.isDoneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    RelativeLayout lineTimeView = (RelativeLayout) arg0.getParent();
                    TextView content = (TextView) lineTimeView.findViewById(R.id.text_content);
                    TextView category = (TextView) lineTimeView.findViewById(R.id.text_category);
                    CheckBox doneBox = (CheckBox) arg0;
                    final boolean isChecked = doneBox.isChecked();

                    ListViewCompat lv = (ListViewCompat) lineTimeView.getParent();
                    int position = lv.getPositionForView(lineTimeView);
                    ((ShoppingListLineAdapter) lv.getAdapter()).getItem(position).toggleDone();

                    if (isChecked) {
                        category.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
                        category.setTypeface(null, Typeface.BOLD_ITALIC);
                        content.setTextColor(ContextCompat.getColor(getContext(),R.color.color_category_done));
                        content.setTypeface(null, Typeface.ITALIC);


                    } else {
                        category.setTextColor(item.getCategory().getColor());
                        category.setTypeface(null,Typeface.BOLD);
                        content.setTextColor(ContextCompat.getColor(getContext(),android.R.color.primary_text_dark));
                        content.setTypeface(null,Typeface.NORMAL);
                    }
                }
            });

        }

        return convertView;
    }


    /**
     * Filtering inspiration from here: http://stackoverflow.com/questions/19122848/custom-getfilter-in-custom-arrayadapter-in-android
     */
    Filter myFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<ShoppingListLine> tempList=new ArrayList<ShoppingListLine>();

            boolean hideDone = (constraint.charAt(0) == '1');
            Log.d(Constants.LOG_TAG,"filtering away completed? "+hideDone);

            List<String> allowedCategories = Arrays.asList(constraint.subSequence(1,constraint.length()).toString().split(","));
            Log.d(Constants.LOG_TAG,"Filtering by categories:"+allowedCategories);


            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(allowedCategories != null && originalData !=null) {
                int length= originalData.size();
                int i=0;
                while(i<length){
                    ShoppingListLine item= originalData.get(i);

                    Log.d(Constants.LOG_TAG,"trialing object:"+item);
                    if (!hideDone || !item.getIsDone()) {
                        if(allowedCategories.contains(item.getCategory().toString())) {
                            Log.d(Constants.LOG_TAG,"good category");
                            tempList.add(item);
                        }
                    }
                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            Log.d(Constants.LOG_TAG,"Result set values from filtering:"+filterResults.values);
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {


            if (results.count > 0) {
                filteredData = (ArrayList<ShoppingListLine>) results.values;
                Log.d(Constants.LOG_TAG,"The filtered data set:"+ filteredData);
            } else {
                filteredData=originalData;
                Log.d(Constants.LOG_TAG,"No data after filtering - uring full data set:"+ filteredData);
            }
            notifyDataSetChanged();
        }
    };

    public Filter getFilter() {
        return myFilter;
    }
}
