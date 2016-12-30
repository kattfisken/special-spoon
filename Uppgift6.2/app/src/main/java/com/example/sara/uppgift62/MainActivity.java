package com.example.sara.uppgift62;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that allows sending an sms.
 */
public class MainActivity extends AppCompatActivity {

    final BroadcastReceiver deliveryReceiver = new DeliveryBroadcastReceiver();
    final BroadcastReceiver sendReceiver = new SendBroadcastReceiver();
    private PendingIntent sentPendingIntent;
    private PendingIntent deliveredPendingIntent;

    /**
     * Shows the main activity.
     *
     * @param savedInstanceState not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sentPendingIntent =
                PendingIntent.getBroadcast(
                        this // This context
                        , 0
                        , new Intent(Const.SMS_SENT) // Intent action of the broadcast
                        , 0);
        deliveredPendingIntent =
                PendingIntent.getBroadcast(
                        this // This context
                        , 0
                        , new Intent(Const.SMS_DELIVERED) // Intent action of the broadcast
                        , 0);
    }

    /**
     * Registers receiver for send and delivery callback.
     */

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(deliveryReceiver, new IntentFilter(Const.SMS_DELIVERED));
        registerReceiver(sendReceiver, new IntentFilter(Const.SMS_SENT));
    }

    /**
     * Unregisters receivers for send and delivery callback.
     */
    @Override
    protected void onStop() {
        unregisterReceiver(deliveryReceiver);
        unregisterReceiver(sendReceiver);

        super.onStop();
    }

    /**
     * Sends sms, if sms content is to long to be sent i one sms an error alert i shown
     * @param view this parameter is needed for xml onClick registration
     */
    public void sendSms(View view) {
        TextView tv = (TextView) findViewById(R.id.new_sms_text);
        String message = tv.getText().toString();
        if (message.length() > 160) {
            Toast.makeText(this, "Text to long, 160 characters is maximum", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView tv2 = (TextView) findViewById(R.id.new_sms_number);
        String number = tv2.getText().toString();
        SmsManager sm = SmsManager.getDefault();


        /**
         * Please not that this app does not handel multipart sms, that's why there is a 160
         * character limit above.
         */
        sm.sendTextMessage(
                number  //Recipient telephone number
                , null // Short message service center. Default is used if parameter = null
                , message // the text message
                , sentPendingIntent // a pending intent that will be broadcasted when sms is sent
                , deliveredPendingIntent);// a pending intent that will be broadcasted when sms is delivered

    }

    /**
     * A broad cast receiver that listens for the sms delivery actions. It presents toast according
     * to the delivery status.
     */
    class DeliveryBroadcastReceiver extends BroadcastReceiver {

        /**
         * When onReceive is called a toast is shown, what it says depends on the broadcast.
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    /**
     * A broad cast receiver that listens for the sms send actions. It presents toast according
     * to the send status.
     */
    class SendBroadcastReceiver extends BroadcastReceiver {
        /**
         * When onReceive is called a toast is shown, what it says depends on the broadcast.
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();

                    clearNumberAndText();


                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Generic SMS failure", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "There were some issues with the SMS."
                            , Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Empties the text views when the sms is sent.
     */
    private void clearNumberAndText() {
        TextView tv = (TextView) findViewById(R.id.new_sms_text);
        TextView tv2 = (TextView) findViewById(R.id.new_sms_number);

        tv.setText("");
        tv2.setText("");
    }
}
