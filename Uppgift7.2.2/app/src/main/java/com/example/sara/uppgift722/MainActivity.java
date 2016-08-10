package com.example.sara.uppgift722;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onClickVibration(View theViewThatWasPressed){
        Vibrator v;
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v.hasVibrator()){
            //vib
            v.vibrate(2000);
            Toast.makeText(MainActivity.this, "Good vibrations!", Toast.LENGTH_SHORT).show();
        }else{
            //message
            Toast.makeText(MainActivity.this, "There is no vibraor installed on your device", Toast.LENGTH_LONG).show();
        }


    }
}


