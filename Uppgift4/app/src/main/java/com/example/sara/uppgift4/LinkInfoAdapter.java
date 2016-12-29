package com.example.sara.uppgift4;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

class LinkInfoAdapter extends ArrayAdapter<LinkInfo> {
    private ArrayList<LinkInfo> data;

    /**
     * Default constructor.
     * @param c The context in which the adapter works. Typically an activity.
     * @param layoutResourceId see ArrayAdapter documentation.
     * @param inData The ArrayList to adapt.
     */
    LinkInfoAdapter(Context c, int layoutResourceId, ArrayList<LinkInfo> inData) {
        super(c, layoutResourceId, inData);
        data = inData;
    }

    /**
     * The method returns the view for list item in position {@code pos}, based on the
     * {@code convertView}.
     * @param pos the integral position in the list
     * @param convertView the view that gets converted into the new presentable view
     * @param parent the parent viewgroup that the new view is mounted in
     * @return the view to be mounted in the listView
     */
    @NonNull
    @Override
    public View getView(int pos, View convertView, @NonNull ViewGroup parent) {

        View  boxWithLinkInfo = convertView;
        if (boxWithLinkInfo == null) {
            boxWithLinkInfo = inflateConvertView();
        }
        final LinkInfo item;
        item = data.get(pos);
        if (item != null) {

            TextView title = (TextView) boxWithLinkInfo.findViewById(R.id.link_title);
            Button url = (Button) boxWithLinkInfo.findViewById(R.id.link_btn);

            title.setText(item.title);
            url.setText(item.data);

            View.OnClickListener l = null;

            if (item.linkType.equals(LinkInfo.WEB_PAGE_LINK)) {

                l = updateViewAndMakeAnOpenWebPageListener(boxWithLinkInfo, item);

            } else if (item.linkType.equals(LinkInfo.YOUTUBE_LINK)) {

                l = startYoutubeVideoListener(item);

            }
            url.setOnClickListener(l);

            ImageButton emailButton = (ImageButton) boxWithLinkInfo.findViewById(R.id.link_email_button);
            ImageButton.OnClickListener listen = new EmailOnClickListener(item);
            emailButton.setOnClickListener(listen);

        }

        return boxWithLinkInfo;

    }

    /**
     * Helper method for inflating the convert view if it would be not inflated.
     * @return an inflated view
     */
    @SuppressLint("InflateParams") // passing the root view to inflation needs more code refactoring...
    private View inflateConvertView() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        return inflater.inflate(R.layout.list_row, null );
    }

    /**
     * This function updates a view that will become a row in the list view with a new symbol that
     * represents a web page link and returns a listener that provides action for clicking the link
     *
     * @param boxWithLinkInfo a view object that will become a row in the list view
     * @param item            the link info object containing the link data
     * @return listener opening a browser in action
     */
    private Button.OnClickListener updateViewAndMakeAnOpenWebPageListener(View boxWithLinkInfo, final LinkInfo item) {
        ImageView picture;
        picture = (ImageView) boxWithLinkInfo.findViewById(R.id.link_symbol);
        picture.setImageResource(android.R.drawable.ic_menu_directions);

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BrowserActivity.class);
                i.putExtra(Constants.LINK_INFO_ITEM, item);
                getContext().startActivity(i);
            }
        };
    }

    /**
     * Factory method for making a new listener that starts a youtube video when called.
     * @param item The LinkInfo item that contains link information.
     * @return The listener.
     */
    private Button.OnClickListener startYoutubeVideoListener(final LinkInfo item) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + item.data));
                if (i.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(i);
                } else {
                    Toast toast;
                    toast = Toast.makeText(getContext(), "There is no youtube video shower installed", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        };
    }

    /**
     * A listener class that starts a new activity of composing an email when called.
     */
    private class EmailOnClickListener implements ImageButton.OnClickListener {
        LinkInfo item;

        /**
         * Default constructor.
         * @param anItem a LinkInfo object containing the link that will be emailed.
         */
        EmailOnClickListener(LinkInfo anItem) {
            item = anItem;
        }

        /**
         * The onClick method. starts a new activity for emailing the link using an intent and
         * passing the link info as an Extra.
         * @param v Only here to fulfil the OnClickListener interface. Not used.
         */
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), EmailActivity.class);
            i.putExtra(Constants.LINK_INFO_ITEM, item);
            getContext().startActivity(i);
        }
    }
}
