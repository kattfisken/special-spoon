package com.example.sara.uppgift4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Main activity for the app. Shows a list of cat links to web pages and youtube videos. The user
 * can also send the links as emails to his/her friends.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Normal overridden activity lifecycle method onCreate.
     * Connects a ListView to a ArrayAdapter for presenting the list of cat links.
     *
     * @param savedInstanceState not used. only included for compatibility.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<LinkInfo> linkInfoArrayList = theListMaker();
        LinkInfoAdapter sara = new LinkInfoAdapter(this, R.layout.list_row, linkInfoArrayList);

        ListView linkListView = (ListView) findViewById(R.id.link_list_view);
        if (linkListView != null) {
            linkListView.setAdapter(sara);
        } else {
            Log.e(Constants.LOG_TAG, "Unexpected error - GUI seems not to have been loaded correctly.");
        }
    }

    /**
     * Method for generating a small database of cat links
     *
     * @return The database. It is hard coded in the java code.
     */
    private ArrayList<LinkInfo> theListMaker() {
        ArrayList<LinkInfo> linkInfoArrayList = new ArrayList<>();
        linkInfoArrayList.add(new LinkInfo(LinkInfo.WEB_PAGE_LINK, "https://www.acc.umu.se/~zqad/cats/index.html", "Kattalogen"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK, "UpJXqaCNzIw", "Cat confused by post it note"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK, "_BRp7ezUqbI", "Cat vs cucumber"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.WEB_PAGE_LINK, "http://www.eatliver.com/brexit-cat/", "Advice cat on Brexit"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK, "sI8NsYIyQ2A", "Why do cats act so wierd"));
        return linkInfoArrayList;
    }


}