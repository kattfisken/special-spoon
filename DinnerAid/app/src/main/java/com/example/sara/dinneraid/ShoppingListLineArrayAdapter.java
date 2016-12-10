package com.example.sara.dinneraid;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ShoppingListLineArrayAdapter extends ArrayAdapter<ShoppingListLine> implements Filterable {

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
                Log.d(Toolbox.LOG_TAG,"removing item: "+originalData.get(j));
                originalData.remove(j);
            } else {
                j++;
            }
        }
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

    public ShoppingListLineArrayAdapter(Context context, int textViewResourceId, ArrayList<ShoppingListLine> data) {
        super(context, textViewResourceId, data);
        filteredData = data;
        originalData = data;
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
            convertView = new ShoppingListLineView(getContext());
        }

        final ShoppingListLine item = getItem(position);
        if (item!= null) {
            ShoppingListLineView v = (ShoppingListLineView) convertView;
            v.setData(item);
            v.render();

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
            Log.d(Toolbox.LOG_TAG,"filtering away completed? "+hideDone);

            List<String> allowedCategories = Arrays.asList(constraint.subSequence(1,constraint.length()).toString().split(","));
            Log.d(Toolbox.LOG_TAG,"Filtering by categories:"+allowedCategories);


            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(allowedCategories.size() > 0 && originalData !=null) {
                int length= originalData.size();
                int i=0;
                while(i<length){
                    ShoppingListLine item= originalData.get(i);

                    Log.d(Toolbox.LOG_TAG,"trialing object:"+item);
                    if (!hideDone || !item.getIsDone()) {
                        if(allowedCategories.contains(item.getCategory().toString())) {
                            Log.d(Toolbox.LOG_TAG,"good category");
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
            Log.d(Toolbox.LOG_TAG,"Result set values from filtering:"+filterResults.values);
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            Log.d(Toolbox.LOG_TAG,"The filtered data set:"+ filteredData);
            filteredData = (ArrayList<ShoppingListLine>) results.values;
            /*
            if (results.count > 0) {


            } else {
                filteredData=originalData;
                Log.d(Toolbox.LOG_TAG,"No data after filtering - using full data set:"+ filteredData);
            }
            */
            notifyDataSetChanged();
        }
    };

    public Filter getFilter() {
        return myFilter;
    }
}
