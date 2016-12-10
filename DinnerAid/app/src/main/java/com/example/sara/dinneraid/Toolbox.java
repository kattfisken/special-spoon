package com.example.sara.dinneraid;


import java.util.ArrayList;

public class Toolbox {

    public static String LOG_TAG = "DinnerAid";

    /**
     * Convenience method for joining string arrays into a delimited list
     * @param array the string array to join
     * @param delimiter what string to put between
     * @return the list
     */
    public static String join(String[] array, String delimiter) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (String item:array) {
            sb.append(prefix);
            prefix=delimiter;
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Convenience method for safe casting of ArrayList<String> to String[]
     * @param stringArrayList input
     * @return output
     */
    public static String[] safeCast(ArrayList<String> stringArrayList) {
        String[] output = new String[stringArrayList.size()]; //allocate space
        output = stringArrayList.toArray(output);
        return  output;
    }
}
