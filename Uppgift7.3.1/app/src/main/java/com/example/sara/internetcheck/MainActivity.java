package com.example.sara.internetcheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "InternetTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateClickAction(findViewById(R.id.root_element));
    }

    public void updateClickAction(View v) {

        //restore to neutral
        ((TextView) findViewById(R.id.text_header)).setText(R.string.neutral_news_title);
        String s = String.format(getString(R.string.neutral_news_text), "The check is not done yet.");
        (findViewById(R.id.root_element)).setBackgroundColor(getResources().getColor(R.color.indicator_neutral));

        new CheckWithGoogleTask().execute(this);
    }

    public void updateTextAndColor(boolean b) {
        Calendar c = Calendar.getInstance();

        int headerR = R.string.bad_news_title; // bad as default
        int bodyR = R.string.bad_news_text;
        int colorR = R.color.indicator_bad;

        if (b) { //potentially change to good
            headerR = R.string.good_news_title;
            bodyR = R.string.good_news_text;
            colorR = R.color.indicator_good;
        }

        String s = String.format(getString(bodyR), c);
        ((TextView) findViewById(R.id.text_body)).setText(s);
        ((TextView) findViewById(R.id.text_header)).setText(headerR);
        (findViewById(R.id.root_element)).setBackgroundColor(getResources().getColor(colorR));
    }

    private class CheckWithGoogleTask extends AsyncTask<Context, Integer, Boolean> {

        /**
         * Check if the host www.google.com can be reached over the internet connections
         * requires the permission INTERNET
         * @param context the app context
         * @return true/false depending if www.google.com can deliver a HTTP response code 200
         */
        public boolean hasActiveInternetConnection(Context context) {
            if (isNetworkAvailable(context)) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
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
         * check if there is any connected network. uses permission "ACCESS_NETWORK_STATE".
         * does not check for internet connection.
         *
         * @param context the context for the app
         * @return true/false depending on netowrk access
         */
        public boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //is null for example when in airplane mode
            if (connectivity == null) {
                return false;
            } else {

                //the GetAllNetworkInfo method was deprecated in API21, but this app targets API15
                // no good alternative is present in API15.
                //noinspection deprecation
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }


            }
            return false;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The context of the activity calling the async task
         * @return true/false depending on whether there is a 200 response from Google.com
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Boolean doInBackground(Context... params) {
            return hasActiveInternetConnection(params[0]);
        }

        protected void onPostExecute(Boolean result) {
            MainActivity.this.updateTextAndColor(result);
        }
    }

}
