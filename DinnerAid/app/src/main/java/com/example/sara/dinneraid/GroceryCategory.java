package com.example.sara.dinneraid;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class GroceryCategory {

    @ColorInt
    private int color;
    private String value;

    GroceryCategory(Context context, String inValue) {
        List<String> allowedCategories = Arrays.asList(context.getResources().getStringArray(R.array.grocery_categories));
        if (allowedCategories.contains(inValue)) {
            value = inValue;

            int[] categoryColors = context.getResources().getIntArray(R.array.category_colors_ids);
            if (allowedCategories.size() != categoryColors.length) {
                Log.e(Toolbox.LOG_TAG, "Couldn't obtain correct nr of colors/names! Is the resource file set up correctly?");
            }
            int categoryNumber = allowedCategories.indexOf(inValue);
            try {
                color = categoryColors[categoryNumber];
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(Toolbox.LOG_TAG, "Couldnt obtain the relevant color");
                color = ContextCompat.getColor(context.getApplicationContext(), R.color.color_category_default);
            }
        } else {
            value = "Unknown";
            color = ContextCompat.getColor(context.getApplicationContext(), R.color.color_category_default);
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
