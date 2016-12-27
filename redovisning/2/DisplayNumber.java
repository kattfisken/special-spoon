package com.example.sara.uppgift2;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for displaying, computing, storing, and loading prime numbers.
 */
public class DisplayNumber extends AppCompatActivity {

    ArrayList<PrimeInfo> allComputedPrimes;
    int currentPrimeIndex;
    final String FILENAME = "list_of_primes";
    final String LOG_TAG = "MahPrimeZ";

    /**
     * A container class for information about prime numbers.
     */
    public static class PrimeInfo implements Serializable{
        long value;
        long millisecondsToCompute;
        Date generationTimeStamp;

        /**
         * Default constructor method. N.B. The index of the prime is not stored.
         * @param primeNumberValue The value of the prime number.
         * @param calcTimeMilliseconds The number of milliseconds it took to comput.
         * @param timeStamp A timestamp on when the prime was computed.
         */
        PrimeInfo(long primeNumberValue, long calcTimeMilliseconds, Date timeStamp) {
            value = primeNumberValue;
            millisecondsToCompute = calcTimeMilliseconds;
            generationTimeStamp = timeStamp;
        }
    }

    /**
     * Normal onCreate method. Set GUI and toolbar. Loads all computed primes from file, if there is
     * any file.
     * @param savedInstanceState not used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_number);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
        } else {
            android.util.Log.e(LOG_TAG,"could not get the action bar for setting 'back' button");
        }

        load_allPrimeInfo();
        currentPrimeIndex = allComputedPrimes.size() -1;
        android.util.Log.d(LOG_TAG,"onCreate method finished. Menu inflated.");
    }


    /**
     * Writes the array of PrimeInfo objects to file. Displays a snackbar with error upon IO errors.
     */
    private void saveAllPrimeInfo()  {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allComputedPrimes);
            oos.close();
            fos.close();
            android.util.Log.d(LOG_TAG,"allComputedPrimes saved successfully");
        } catch (IOException i)
        {
            String errorMessage = "Couldn't serialize and write the initial prime list";
            android.util.Log.e(LOG_TAG,errorMessage);
            View v = findViewById(R.id.displayNumberRootView);
            if (v!=null) {
                Snackbar sB = Snackbar.make(v, errorMessage, Snackbar.LENGTH_SHORT);
                sB.show();
            } else {
                Log.e(LOG_TAG,"could not get the root view element");
            }
        }

    }

    /**
     * Load all known primes from file. On errors, the file is reset to only hold the 3 initial
     * prime numbers 0,1 and 3.
     */
    private void load_allPrimeInfo() {
        allComputedPrimes = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // below is an unchecked cast. thats ok since the file only holds one object type
            //noinspection unchecked
            allComputedPrimes = (ArrayList<PrimeInfo>) ois.readObject();
            ois.close();
            fis.close();
            Log.d(LOG_TAG,"loaded prime info successfully");
        } catch (IOException i) {
            Log.e(LOG_TAG,"Couldn't load the initial prime list. Generating new data file.");
            resetPrimeFile();
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG,"Couldn't identify the class in the stored file");
            resetPrimeFile();
        }
    }

    /**
     * Reset the file of known primes to only hold 0, 1 and 3.
     */
    private void resetPrimeFile() {
        allComputedPrimes = new ArrayList<>();
        allComputedPrimes.add(new PrimeInfo(0, 0, new Date()));
        allComputedPrimes.add(new PrimeInfo(1, 0, new Date()));
        allComputedPrimes.add(new PrimeInfo(3, 0, new Date()));
        saveAllPrimeInfo();
    }

    /**
     * Show the data for a computed prime number. (value, computation time, computation timestamp)
     * Method does not check that the value is computed before hand. You should be prepared to
     * catch an ArrayIndexOutOfBoundsException unless you are careful.
     * @param primeIndex the index of the prime number. Index 3 gives value 5 etc...
     */
    public void presentPrime (int primeIndex) {
        TextView tv;
        tv = (TextView) findViewById(R.id.prime_text_view);

        if (tv != null) {
            PrimeInfo currentPrimeInfo = allComputedPrimes.get(primeIndex);

            String textToView = String.format(Locale.getDefault(),getString(R.string.prime_display_format_string)
                ,primeIndex
                ,currentPrimeInfo.value
                ,currentPrimeInfo.millisecondsToCompute
                ,currentPrimeInfo.generationTimeStamp);

            tv.setText(textToView);
        } else {
            Log.e(LOG_TAG,"could not find the text view for presenting a prime");
        }


    }


    /**
     * Shows previous prime. Updates the GUI so the app shows the prime number with one lower index.
     * If attempting to show the -1'th prime, a snackbar is shown.
     */
    public void presentPreviousPrime () {
        if (currentPrimeIndex > 0) {
            currentPrimeIndex--;
            presentPrime(currentPrimeIndex);
        } else {


            Log.e(LOG_TAG,getString(R.string.error_too_small_prime_index));
            View v = findViewById(R.id.displayNumberRootView);
            if (v!=null) {
                Snackbar sB = Snackbar.make(v, getString(R.string.error_too_small_prime_index), Snackbar.LENGTH_SHORT);
                sB.show();
            } else {
                Log.e(LOG_TAG,"could not get the root view element");
            }
        }

    }

    /**
     * Display next prime in the GUI. If needed - the method asks for the next prime to be computed.
     */
    public void presentNextPrime () {
        if (currentPrimeIndex == (allComputedPrimes.size()-1)) {
            computeNextPrime();
        }
        currentPrimeIndex++;
        presentPrime(currentPrimeIndex);
    }

    /**
     * Compute the next prime number and save the result to file.
     */
    private void computeNextPrime() {
        long timeBefore = System.currentTimeMillis();
        long candidate =  allComputedPrimes.get(allComputedPrimes.size()-1).value+2;
        boolean candidateIsPrime = false;

        while (!candidateIsPrime) {
            candidateIsPrime = true;
            for(int i = 3; i <= Math.sqrt(candidate) ; i+=2) {

                if (candidate % i == 0 ) {
                    candidateIsPrime = false;
                    candidate += 2;
                    break;
                }

            }
        }

        long timeAfter = System.currentTimeMillis();
        long elapsedTime = timeAfter - timeBefore;
        Log.d(LOG_TAG,"The computation of next prime took "+elapsedTime+" milliseconds");

        allComputedPrimes.add(new PrimeInfo(candidate,elapsedTime, new Date()));
        saveAllPrimeInfo();
    }


    /**
     * Standard inflation of menu items.
     * @param menu not overridden. see superclass.
     * @return not overridden. see superclass.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * listener for menu clicks.
     * @param item the item that was clicked in the menu.
     * @return not overridden. see superclass.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btn_prev_prime:
                presentPreviousPrime();
                return true;

            case R.id.btn_next_prime:
                presentNextPrime();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                android.util.Log.i(LOG_TAG,"Unrecognized user action in menu.");
                return super.onOptionsItemSelected(item);

        }
    }

}
