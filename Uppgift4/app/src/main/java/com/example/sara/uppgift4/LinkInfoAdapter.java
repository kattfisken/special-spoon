package com.example.sara.uppgift4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ludvig on 2016-08-02.
 */
public class LinkInfoAdapter extends ArrayAdapter<LinkInfo> {
        ArrayList<LinkInfo> data;
        public LinkInfoAdapter(Context c, int layoutResourceId, ArrayList <LinkInfo> inData){
            super (c, layoutResourceId, inData);
            data = inData;

        }
        public View getView(int pos, View convertView, ViewGroup parent){
            View boxWithLinkInfo;
            boxWithLinkInfo = convertView;
            if (boxWithLinkInfo == null){
                Object inflaterAsObject;
                inflaterAsObject = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater inflater;
                inflater = (LayoutInflater) inflaterAsObject;
                boxWithLinkInfo = inflater.inflate(R.layout.list_row, null);
            }
            final LinkInfo item;
            item = data.get(pos);
            if (item != null) {
                TextView title;
                Button url;
                title = (TextView)boxWithLinkInfo.findViewById(R.id.link_title);
                url = (Button) boxWithLinkInfo.findViewById(R.id.link_btn);
                title.setText(item.title);
                url.setText(item.url);
                View.OnClickListener l = null;

                if (item.linkType == LinkInfo.WEB_PAGE_LINK){
                    ImageView picture;
                    picture = (ImageView)boxWithLinkInfo.findViewById(R.id.link_symbol);
                    picture.setImageResource(android.R.drawable.ic_menu_directions);

                    //todo add on-click action
                    l = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder popUp;
                            popUp = new AlertDialog.Builder(getContext());
                            popUp.setMessage("hej hopp gummi snopp");
                            popUp.show();
                        }
                    };
                    }
                else if (item.linkType == LinkInfo.YOUTUBE_LINK ){
                    //todo add on-click action
                    l = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i;
                            i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+item.url));
                            getContext().startActivity(i);
                        }
                    };
                }

                url.setOnClickListener(l);
            }

            return boxWithLinkInfo;

        }


    }
