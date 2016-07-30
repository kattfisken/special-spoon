package com.example.sara.uppgift2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        Button buttonOne = (Button) findViewById(R.id.number_act_btn);
        if(buttonOne != null) {
            buttonOne.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            Intent lillaI;
                            lillaI = new Intent(WelcomeScreen.this, DisplayNumber.class);
                            WelcomeScreen.this.startActivity(lillaI);
                        }
                    }
            );
        }else {
            Log.e("Uppgift2","failed to retrive the button for setting action");
        }

    }

}
