package com.example.sara.uppgift721;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {

    SectionsPagerAdapter mFragmentAdapter;

    static final String LOGTAG = "Uppgift 7.2.1";

    /**
     * set up fragment adapter with viewpager - i.e. set up the GUI.
     * @param savedInstanceState not used.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create the adapter that will return a fragment for each of the sections in the activity
        mFragmentAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //set up the viewPager with the sections adapter
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mFragmentAdapter);

        // set the tab-menu to correspond to the fragments
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

}
