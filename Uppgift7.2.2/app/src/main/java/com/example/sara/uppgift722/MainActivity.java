package com.example.sara.uppgift722;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * The applications only activity. Starts when the app button is pressed.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Draws user interface according to xml layout.
     *
     * @param savedInstanceState not used, only there for compatibility
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Makes the mobile phone vibrate in 2 s or sends a messages if there is no vibrator installed.
     *
     * @param theViewThatWasPressed a view parameter to enable "xml button onclick connection".
     */
    public void onClickVibration(View theViewThatWasPressed) {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v.hasVibrator()) {
            v.vibrate(2000);
            Toast.makeText(MainActivity.this, "Good vibrations!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "There is no vibrator installed on your device", Toast.LENGTH_LONG).show();
        }


    }
}


