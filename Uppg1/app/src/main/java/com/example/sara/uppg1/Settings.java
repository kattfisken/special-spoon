package com.example.sara.uppg1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Sara on 2016-07-26.
 */
public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public void switchToNameGenerator(View currentView){
        Intent myIntent=new Intent(this,NameGenerator.class);
        startActivity(myIntent);
    }
}
