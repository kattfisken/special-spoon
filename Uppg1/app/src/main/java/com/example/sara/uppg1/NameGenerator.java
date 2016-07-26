package com.example.sara.uppg1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NameGenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_generator);
    }
    public void switchToSettings(View v){
        Intent ludvig=new Intent(this, Settings.class);
        startActivity(ludvig);
    }

}
