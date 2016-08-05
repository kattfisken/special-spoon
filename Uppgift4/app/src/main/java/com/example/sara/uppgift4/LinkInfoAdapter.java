package com.example.sara.uppgift4;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
                boxWithLinkInfo = inflateConvertView();
            }
            final LinkInfo item;
            item = data.get(pos);
            if (item != null) {

                TextView title = (TextView)boxWithLinkInfo.findViewById(R.id.link_title);
                Button url = (Button) boxWithLinkInfo.findViewById(R.id.link_btn);

                title.setText(item.title);
                url.setText(item.data);

                View.OnClickListener l = null;

                if (item.linkType == LinkInfo.WEB_PAGE_LINK){

                    l = updateViewForWebPage(boxWithLinkInfo, item);

                } else if (item.linkType == LinkInfo.YOUTUBE_LINK ){

                    l = updateViewForYoutubeVideo(boxWithLinkInfo, item);

                }
                url.setOnClickListener(l);

                ImageButton emailButton = (ImageButton) boxWithLinkInfo.findViewById(R.id.link_email_button);
                ImageButton.OnClickListener listen = new EmailOnClickListener("Hej Hopp Gumisnopp", item);
                emailButton.setOnClickListener(listen);

            }

            return boxWithLinkInfo;

        }

        private View inflateConvertView(){
            Object inflaterAsObject;
            inflaterAsObject = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater;
            inflater = (LayoutInflater) inflaterAsObject;
            return inflater.inflate(R.layout.list_row, null);
        }

        private Button.OnClickListener updateViewForWebPage(View boxWithLinkInfo, final LinkInfo item) {
            ImageView picture;
            picture = (ImageView)boxWithLinkInfo.findViewById(R.id.link_symbol);
            picture.setImageResource(android.R.drawable.ic_menu_directions);

            Button.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(),BrowserActivity.class);
                    i.putExtra(Constants.LINK_INFO_ITEM,item);
                    getContext().startActivity(i);
                }
            };
            return l;
        }

    private Button.OnClickListener updateViewForYoutubeVideo(View boxWithLinkInfo, final LinkInfo item) {
        Button.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + item.data));
                if (i.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(i);
                }else{
                    Toast toast;
                    toast = Toast.makeText(getContext(),"There is no youtube video shower installed",Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        };

        return l;
    }

    private class EmailOnClickListener implements ImageButton.OnClickListener  {
        String message;
        LinkInfo item;

        public EmailOnClickListener(String aMessage, LinkInfo anItem){
            message = aMessage;
            item = anItem;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(),EmailActivity.class);
            i.putExtra(Constants.LINK_INFO_ITEM,item);
            getContext().startActivity(i);
        }
    }
}
