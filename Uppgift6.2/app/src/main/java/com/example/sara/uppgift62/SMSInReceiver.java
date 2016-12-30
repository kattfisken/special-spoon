package com.example.sara.uppgift62;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Shows a toast with a newly received SMS when the system gets an SMS.
 */
public class SMSInReceiver extends BroadcastReceiver {
    /**
     * Receives a broad cast that an SMS has been received and show a toast that tells you that you
     * have received a SMS.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(Const.LOG_TAG, " Onreceive method started");

        final Bundle dataBundle = intent.getExtras();
        try {
            final byte[][] pdus = (byte[][]) dataBundle.get("pdus");

            if (pdus != null) {
                for (byte[] pdu : pdus) {

                    SmsMessage currentMessage;

                    // deprecated function needs to be used on API <= 22
                    //noinspection deprecation
                    currentMessage = SmsMessage.createFromPdu(pdu);

                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message" + message);

                    Toast.makeText(context, "senderNum: " + phoneNumber + "; message" + message,
                            Toast.LENGTH_LONG).show();

                }
            } else {
                Log.e(Const.LOG_TAG, "SMS broadcast received but there is no pdus in the bundle.");
            }


        } catch (Exception e) {
            Log.e(Const.LOG_TAG, "Exception in smsReceiver" + e);
            e.printStackTrace();
        }

    }
}
