package com.example.sara.uppgift4;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList <LinkInfo> linkInfoArrayList = theListMaker();
        LinkInfoAdapter sara = new LinkInfoAdapter(this,R.layout.list_row, linkInfoArrayList);

        ListView linkListView;
        View linkListViewAsView;
        linkListViewAsView = findViewById(R.id.link_list_view);
        linkListView = (ListView)linkListViewAsView;
        linkListView.setAdapter(sara);
    }

    /**
     * Method for generating a small database of cat links
     * @return the database
     */
    private ArrayList<LinkInfo> theListMaker() {
        ArrayList<LinkInfo> linkInfoArrayList = new ArrayList<LinkInfo>();
        linkInfoArrayList.add(new LinkInfo(LinkInfo.WEB_PAGE_LINK,"https://www.acc.umu.se/~zqad/cats/index.html","Kattalogen" ));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK,"UpJXqaCNzIw","Cat confused by post it note" ));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK,"_BRp7ezUqbI","Cat vs cucumber"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.WEB_PAGE_LINK,"http://www.eatliver.com/brexit-cat/","Advice cat on Brexit"));
        linkInfoArrayList.add(new LinkInfo(LinkInfo.YOUTUBE_LINK,"sI8NsYIyQ2A","Why do cats act so wierd"));
        return linkInfoArrayList;
    }


}