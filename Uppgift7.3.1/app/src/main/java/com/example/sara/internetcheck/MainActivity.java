package com.example.sara.internetcheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * An activity that shows a screen. First the screen is yellow saying "maybe" about connection
 * status but after a short while it says "yes" or "no" depending on connection status.
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "InternetTest";

    public enum ConnectionStatus {GOOD, BAD, NEUTRAL}


    /**
     * set the GUI and update the connection status report in the main screen.
     *
     * @param savedInstanceState not in use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateClickAction(null);
    }

    /**
     * Set the screen to neutral state (neutral color and "maybe" text) and then re-query if we have
     * connection to internet.
     *
     * @param v not used. Only for XML onClick registration. May be Null.
     */
    public void updateClickAction(@Nullable View v) {

        //restore header to neutral
        updateTextAndColor(ConnectionStatus.NEUTRAL);

        // ask background method to check the internet connection
        new CheckWithGoogleTask().execute(this);
    }

    /**
     * Sets the background color and the text in the main window depending on input connectionStatus.
     *
     * @param connectionStatus Enum values good, bad or neutral.
     */
    public void updateTextAndColor(ConnectionStatus connectionStatus) {
        Calendar c = Calendar.getInstance();

        int headerResourceId;
        int textResourceId;
        int colorResourceId;
        String s;

        switch (connectionStatus) {
            case BAD:
                headerResourceId = R.string.bad_news_title;
                textResourceId = R.string.bad_news_text;
                colorResourceId = R.color.indicator_bad;
                s = String.format(getString(textResourceId), c);
                break;
            case GOOD:
                headerResourceId = R.string.good_news_title;
                textResourceId = R.string.good_news_text;
                colorResourceId = R.color.indicator_good;
                s = String.format(getString(textResourceId), c);
                break;
            case NEUTRAL:
            default:
                headerResourceId = R.string.neutral_news_title;
                textResourceId = R.string.neutral_news_text;
                colorResourceId = R.color.indicator_neutral;
                s = String.format(getString(textResourceId), "The check is not done yet.");
                break;

        }

        ((TextView) findViewById(R.id.text_body)).setText(s);
        ((TextView) findViewById(R.id.text_header)).setText(headerResourceId);
        int color;
        if (Build.VERSION.SDK_INT >= 23) {
            color = getResources().getColor(colorResourceId, null);
        } else {
            //noinspection deprecation
            color = getResources().getColor(colorResourceId);
        }
        (findViewById(R.id.root_element)).setBackgroundColor(color);
    }

    /**
     * Class for making http response to google asynchronously.
     */
    private class CheckWithGoogleTask extends AsyncTask<Context, Integer, Boolean> {

        /**
         * Check if the host www.google.com can be reached over the internet connections.
         * Requires the permission INTERNET.
         *
         * @param context The app context.
         * @return true/false depending if www.google.com can deliver a HTTP response code 200
         */
        boolean hasActiveInternetConnection(Context context) {
            if (isNetworkAvailable(context)) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com")
                            .openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error checking internet connection", e);
                    e.printStackTrace();
                }
            } else {
                Log.d(LOG_TAG, "No network available!");
            }
            return false;
        }

        /**
         * Check if there is any connected network. uses permission "ACCESS_NETWORK_STATE".
         * Does not check for <em>internet</em> connection - only network connection.
         *
         * @param context the context for the app.
         * @return true/false depending on network access.
         */
        boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //is null for example when in airplane mode
            if (connectivity == null) {
                return false;
            } else {

                //the GetAllNetworkInfo method was deprecated in API21, but this app targets API15
                // no good alternative is present in API15.
                //noinspection deprecation
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }


            }
            return false;
        }

        /**
         * This method performs a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * When finished, this method calls {@link #onPostExecute} in the GUI thread.
         *
         * @param params The context of the activity calling the async task
         * @return true/false depending on whether there is a 200 response from Google.com
         * @see #onPostExecute
         */
        @Override
        protected Boolean doInBackground(Context... params) {
            return hasActiveInternetConnection(params[0]);
        }

        /**
         * Update the GUI according to result in asynchronous task.
         * This task is run automatically when async task is done.
         *
         * @param result True if we have internet connection. Otherwise False.
         */
        protected void onPostExecute(Boolean result) {
            if (result) {
                MainActivity.this.updateTextAndColor(ConnectionStatus.GOOD);
            } else {
                MainActivity.this.updateTextAndColor(ConnectionStatus.BAD);
            }
        }
    }

}
