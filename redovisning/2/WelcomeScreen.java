package com.example.sara.uppgift2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Default activity. A welcome screen that explains tha app. When pressing the button in the GUI,
 * a new activity is started.
 */
public class WelcomeScreen extends AppCompatActivity {

    /**
     * On create method. The normal activity initialization. Also creates a listener for the button
     * which closes the welcome screen.
     * @param savedInstanceState not used. only for compatibility.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        Button startButton = (Button) findViewById(R.id.number_act_btn);
        if(startButton != null) {
            startButton.setOnClickListener(
                    /**
                     * anonymous listener class.
                     */
                    new Button.OnClickListener() {
                        /**
                         * the method that starts the DisplayNumber activity.
                         * @param v
                         */
                        public void onClick(View v) {
                            Intent intent = new Intent(WelcomeScreen.this, DisplayNumber.class);
                            WelcomeScreen.this.startActivity(intent);
                        }
                    }
            );
        }else {
            Log.e("MahPrimeZ","failed to retrive the button for setting action");
        }

    }

}
