package com.example.sara.uppgift4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


/**
 * The email activity. In this activity you can write an email.
 */
public class EmailActivity extends AppCompatActivity {

    /**
     * Normal life cycle method. Populates the email form with data from the LinkInfo object passed
     * via the intent.
     *
     * @param savedInstanceState not in use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(Constants.LOG_TAG, "Something went wrong... The action bar seems to not have " +
                    "been loaded.");
        }


        LinkInfo item = (LinkInfo) getIntent().getExtras().getSerializable(Constants.LINK_INFO_ITEM);
        String s;
        if (item != null) {
            s = "Look at this amazing cat link:\n" + item.getUrl();
        } else {
            Log.d(Constants.LOG_TAG, "couldn't find item");
            s = "Something went wrong. Type cat link here.";
        }

        EditText body = (EditText) findViewById(R.id.input_body);
        if (body != null) {
            body.setText(s);
        } else {
            Log.d(Constants.LOG_TAG, "couldn't find body tag");
        }


    }

    /**
     * Callback function to be called when StartActivityForResult is finished e.g. choosing
     * attachment or sending email.
     *
     * @param requestCode An integer defined in class Constant identifying what activity is finished.
     * @param resultCode  Result code according to Androids standard.
     * @param data        The result data from other activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Constants.ATTACHMENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    AttachmentButton ab = (AttachmentButton) findViewById(R.id.email_btn_attachment);
                    if (ab != null) {
                        ab.addFile(data.getData());
                    } else {
                        Log.e(Constants.LOG_TAG, "Unexpected error. Attachment Button not found");
                    }
                } else {
                    View v = findViewById(R.id.email_header);
                    if (v != null) {
                        (Snackbar.make(v, R.string.snackbar_no_attachment, Snackbar.LENGTH_LONG))
                                .show();
                    }
                }
                break;

            case Constants.SEND_EMAIL_REQUEST_CODE:
                NavUtils.navigateUpFromSameTask(this); // go to parent activity i.e. link list.
                break;

        }
    }

    /**
     * Check if a CharSequence is a valid email.
     *
     * @param target The suspected email address.
     * @return True if the address is indeed a valid email address.
     */
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Checks if a list of strings is a list of valid emails
     *
     * @param emails an array of emails (?)
     * @return True if all strings are email addresses.
     */
    public boolean areValidEmails(String[] emails) {
        for (String email : emails) {
            if (!isValidEmail(email)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Packs all information from the form into an intent and passes it to an emailing app.
     *
     * @param v not used. obligatory vierw parameter for xml OnClick registration.
     */
    public void sendEmail(View v) {

        EditText textFieldTo = (EditText) findViewById(R.id.input_to);
        EditText textFieldSubject = (EditText) findViewById(R.id.input_title);
        EditText textFieldBody = (EditText) findViewById(R.id.input_body);
        AttachmentButton ab = (AttachmentButton) findViewById(R.id.email_btn_attachment);

        if (textFieldBody == null ||
                textFieldSubject == null ||
                textFieldTo == null ||
                ab == null) {
            Log.e(Constants.LOG_TAG, "GUI error? Couldnt obtain email content data from views");
            return;
        }

        String to_input = textFieldTo.getText().toString();
        String subject = textFieldSubject.getText().toString();
        String body = textFieldBody.getText().toString();
        Uri fileUri = ab.attachmentUri;

        String[] recipients = to_input.split(";");
        for (int i = 0; i < recipients.length; i++)
            recipients[i] = recipients[i].trim();


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        //this is a super old message type for emails...
        // That makes almost only email apps respond to the intent.
        // the "normal" way to make only email apps respond is running the command
        // i.setData(Uri.parse("mailto:"));
        // but that does not work on my old Galaxy S2 device... (bad intent filters ?? )
        // so above is kind of a hack... it works so so


        if (!areValidEmails(recipients)) {
            Snackbar sb = Snackbar.make(v, "Check recipient email format", Snackbar.LENGTH_LONG);
            sb.show();
            Log.d(Constants.LOG_TAG, "bad target email(s)");
        } else {


            i.putExtra(Intent.EXTRA_EMAIL, recipients);
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, body);


            if (fileUri != null) {
                i.putExtra(Intent.EXTRA_STREAM, fileUri);
            } else {
                Log.d(Constants.LOG_TAG, "no attachment chosen");
            }


            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, Constants.SEND_EMAIL_REQUEST_CODE);
            } else {
                (Snackbar.make(
                        textFieldBody
                        , R.string.sb_text_no_email_client
                        , Snackbar.LENGTH_INDEFINITE)).show();
            }
        }
    }
}
