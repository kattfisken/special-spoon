package com.example.sara.dinneraid;

import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class GroceryCategory {

    private ShoppingActivity shoppingActivity;
    @ColorInt
    private int color;
    private String value;

    GroceryCategory(ShoppingActivity shoppingActivity, String inValue) {
        this.shoppingActivity = shoppingActivity;
        List<String> allowedCategories = Arrays.asList(shoppingActivity.getResources().getStringArray(R.array.grocery_categories));
        if (allowedCategories.contains(inValue)) {
            value = inValue;

            int[] categoryColors = shoppingActivity.getResources().getIntArray(R.array.category_colors_ids);
            if (allowedCategories.size() != categoryColors.length) {
                Log.e(Constants.LOG_TAG, "Couldn't obtain correct nr of colors/names! Is the resource file set up correctly?");
            }
            int categoryNumber = allowedCategories.indexOf(inValue);
            try {
                color = categoryColors[categoryNumber];
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(Constants.LOG_TAG, "Couldnt obtain the relevant color");
                color = ContextCompat.getColor(shoppingActivity.getApplicationContext(), R.color.color_category_default);
            }
        } else {
            value = "Unknown";
            color = ContextCompat.getColor(shoppingActivity.getApplicationContext(), R.color.color_category_default);
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

}
