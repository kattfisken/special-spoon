package com.example.sara.dinneraid;


import java.util.ArrayList;

/**
 * A helper class with Java-generic helping functions.
 */
class Toolbox {

    static String LOG_TAG = "DinnerAid";

    /**
     * Convenience method for joining string arrays into a delimited list
     *
     * @param array     the string array to join
     * @param delimiter what string to put between
     * @return the list
     */
    static String join(String[] array, String delimiter) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (String item : array) {
            sb.append(prefix);
            prefix = delimiter;
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Convenience method for safe casting of ArrayList<String> to String[]
     *
     * @param stringArrayList input
     * @return output
     */
    private static String[] safeCast(ArrayList<String> stringArrayList) {
        String[] output = new String[stringArrayList.size()]; //allocate space
        output = stringArrayList.toArray(output);
        return output;
    }

    /**
     * A method that takes to arrays of equal length and returns a (potentially shorter) list based
     * on the true/false values in the boolean array
     *
     * @param sArr String array to filter
     * @param bArr Bool array that holds filter flags.
     * @return The new, (possibly shorter) String array.
     */
    static String[] filterByBoolList(String[] sArr, boolean[] bArr) {

        ArrayList<String> output = new ArrayList<>();
        if (sArr.length == bArr.length) {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr[i]) {
                    output.add(sArr[i]);
                }
            }
        }
        return safeCast(output);
    }

    /**
     * A method that checks if any of the values in a bool array is false.
     *
     * @param arr input to check
     * @return False if the whole array is true. Otherwise True.
     */
    static boolean anyFalse(boolean[] arr) {
        for (boolean anArr : arr) {
            if (!anArr) {
                return true;
            }
        }
        return false;
    }
}
