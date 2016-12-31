package com.example.sara.dinneraid;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * A class representing the possible categories. Could maybe have been implemented as an Enum, but
 * using this class provides a simple interface via a android resource string array.
 */
class GroceryCategory {

    @ColorInt
    private int color;
    private String value;

    /**
     * Default constructor. Makes sure the category exist in android resource xml files and assigns
     * a relevant color to the category.
     *
     * @param context The app context. Must be passsed to allow access to android resources.
     * @param inValue The name of the category to create.
     */
    GroceryCategory(Context context, String inValue) {
        List<String> allowedCategories =
                Arrays.asList(context.getResources().getStringArray(R.array.grocery_categories));

        if (allowedCategories.contains(inValue)) {
            value = inValue;

            int[] categoryColors = context.getResources().getIntArray(R.array.category_colors_ids);
            if (allowedCategories.size() != categoryColors.length) {
                Log.e(Toolbox.LOG_TAG, "Couldn't obtain correct nr of colors/names!" +
                        " Is the resource file set up correctly?");
            }
            int categoryNumber = allowedCategories.indexOf(inValue);
            try {
                color = categoryColors[categoryNumber];
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(Toolbox.LOG_TAG, "Couldnt obtain the relevant color");
                color = ContextCompat.getColor(context.getApplicationContext()
                        , R.color.color_category_default);
            }
        } else {
            value = context.getString(R.string.default_category_text);
            color = ContextCompat.getColor(context.getApplicationContext(), R.color.color_category_default);
        }
    }

    /**
     * Get the name of the category as string.
     *
     * @return The name of the category.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * An int representing the color of the category.
     *
     * @return a Color int for the category color.
     */
    @ColorInt
    public int getColor() {
        return color;
    }

}
