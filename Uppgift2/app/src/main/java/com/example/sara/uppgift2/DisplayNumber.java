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

public class DisplayNumber extends AppCompatActivity {

    ArrayList<PrimeInfo> allPrimeInfo;
    int current_prime_index;
    final String FILENAME = "list_of_primes";
    final String LOG_TAG = "Uppgift2";

    public static class PrimeInfo implements Serializable{
        public long value;
        public double calcTime_millis;
        public Date generationTimeStamp;

        public PrimeInfo(long value_in, double calctime_in, Date ts_in) {
            value = value_in;
            calcTime_millis = calctime_in;
            generationTimeStamp = ts_in;
        }
    }

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

        // current_prime_index = 2;
        load_allPrimeInfo();
        current_prime_index = allPrimeInfo.size() -1;
        android.util.Log.d(LOG_TAG,"onCreate method finished. menu inflated.");
    }

    private void save_allPrimeInfo()  {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allPrimeInfo);
            oos.close();
            fos.close();
            android.util.Log.d(LOG_TAG,"allPrimeInfo saved successfully");
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

    private void load_allPrimeInfo() {
        allPrimeInfo = new ArrayList<PrimeInfo>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // below is an unchecked cast. thats ok since the file only holds one object type
            //noinspection unchecked
            allPrimeInfo = (ArrayList<PrimeInfo>) ois.readObject();
            ois.close();
            fis.close();
            android.util.Log.d(LOG_TAG,"loaded prime info successfully");
        } catch (IOException i)
        {
            android.util.Log.e(LOG_TAG,"Couldn't load the initial prime list. Generating new data file.");
            allPrimeInfo.add(new PrimeInfo(0, 0, new Date()));
            allPrimeInfo.add(new PrimeInfo(1, 0, new Date()));
            allPrimeInfo.add(new PrimeInfo(3, 0, new Date()));
            save_allPrimeInfo();
        } catch (ClassNotFoundException e) {
            android.util.Log.e(LOG_TAG,"Couldn't indentify the class in the stored file");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presentPrime(current_prime_index);
        android.util.Log.d(LOG_TAG,"finished the onStart invocation");
    }


    public void presentPrime (int prime_index) {
        TextView tv;
        tv = (TextView) findViewById(R.id.prime_text_view);

        if (tv != null) {
            PrimeInfo currentPrimeInfo = allPrimeInfo.get(prime_index);
            String textToView = "The " +
                    prime_index + "th prime is " +
                    currentPrimeInfo.value + ". It took " +
                    currentPrimeInfo.calcTime_millis / 1000 + " seconds to compute it, and was finished on " +
                    currentPrimeInfo.generationTimeStamp + ".";
            tv.setText(textToView);
        } else {
            Log.e(LOG_TAG,"could not find the text view for presenting a prime");
        }


    }


    public void presentPreviousPrime () {
        if (current_prime_index > 0) {
            current_prime_index--;
            presentPrime(current_prime_index);
        } else {

            String errorMessage = "Smallest prime reached. User attempts finding smaller prime.";
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

    public void presentNextPrime () {

        if (current_prime_index == (allPrimeInfo.size()-1)) {

            long timeBefore = System.currentTimeMillis();
            long candidate =  allPrimeInfo.get(current_prime_index).value+2;
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

            allPrimeInfo.add(new PrimeInfo(candidate,elapsedTime, new Date()));
            save_allPrimeInfo();

        }
        current_prime_index++;
        presentPrime(current_prime_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


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
