package com.example.sara.uppgift2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DisplayNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_number);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* todo skapa en fil att spara data i */



    }

    @Override
    protected void onStart() {
        super.onStart();

        /* todo rita upp textrutan med info om största primtalet och hur lång tid det tog att räkna fram det  */
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        /* todo öppna koppling till filen igen */
    }


    @Override
    protected void onStop() {
        super.onStop();

        /* todo stäng filen med senaste funna talet */
    }


    public void presentPreviousPrime (View v) {
        /* todo hämta info från arrayen om primtalet innan
         * todo rita upp det i textrutan */

    }

    public void presentNextPrime (View v) {
        /* todo försök hämta nästa element från arrayen
         * om elementet finns så visa upp det
         * om elementet inte finns så rita en "calculating next prime-vänte-ruta", och beräkna därefter nästa primtal
         *  när elementet är beräknat så spara ner info om ordningsnummer, värde och beäkningstid och beräkningsdag (när blev det klart)
         * när */


    }

}
