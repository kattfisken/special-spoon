package com.example.sara.uppgift4;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        EditText title = (EditText) findViewById(R.id.input_title);
        EditText body = (EditText) findViewById(R.id.input_body);

        Bundle b = getIntent().getExtras();
        LinkInfo item = (LinkInfo) b.getSerializable(Constants.LINK_INFO_ITEM);


//todo gör genomtänkt
        //title.setText("");
        String s = "Email body default text";
        if (item != null) {
            s = "Look at this amazing cat link: " +
                    "url:" + item.getUrl() + "\n" +
                    "title:" + item.title + "\n" +
                    "type:" + item.linkType + "\n" +
                    "";
        } else {
            Log.d(Constants.LOG_TAG, "couldn't find item");
        }
        if (body != null) {
            body.setText(s);
        } else {
            Log.d(Constants.LOG_TAG, "couldn't find body tag");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.ATTACHMENT_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                AttachmentButton ab = (AttachmentButton) findViewById(R.id.email_btn_attachment);
                Uri uri = data.getData();
                ab.addFile(uri);
            } else {
                (Snackbar.make(findViewById(R.id.email_header), R.string.snackbar_no_attachment,Snackbar.LENGTH_LONG)).show();
            }

        } else if (requestCode == Constants.SEND_EMAIL_REQUEST_CODE) {
            // no need to check RESULT_CODE since it is always 0 for the SEND activity on mail apps.
            NavUtils.navigateUpFromSameTask(this); // go to parent activity i.e. link list.
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean areValidEmails(String[] emails){
        for(int i=0; i < emails.length;i++){
            if(!isValidEmail(emails[i])){
                return false;
            }
        }
        return true;
    }

    public void sendEmail(View v) {


        String to_input = ((EditText) findViewById(R.id.input_to)).getText().toString();
        String[] recipients = to_input.split(";");
        for (int i = 0; i < recipients.length; i++)
            recipients[i] = recipients[i].trim();

        String title = ((EditText) findViewById(R.id.input_title)).getText().toString();
        String body = ((EditText) findViewById(R.id.input_body)).getText().toString();
        Uri fileUri = ((AttachmentButton) findViewById(R.id.email_btn_attachment)).attachmentUri;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822"); //this is a super old MIME type for emails... That makes almost only email apps respond to the intent.
        // the "normal" way to make only email apps respond is running the command i.setData(Uri.parse("mailto:"));
        // but that does not work on my old Galaxy S2 device... (bad intent filters ?? )



        if (!areValidEmails(recipients)) {
            Snackbar sb = Snackbar.make(v, "Check recipient email format", Snackbar.LENGTH_INDEFINITE);
            sb.show();
           Log.d(Constants.LOG_TAG,"bad target email(s)");
        } else {



            i.putExtra(Intent.EXTRA_EMAIL,recipients);
            i.putExtra(Intent.EXTRA_SUBJECT, title);
            i.putExtra(Intent.EXTRA_TEXT, body);


            if (fileUri != null) {
                i.putExtra(Intent.EXTRA_STREAM, fileUri);
            } else {
                Log.d(Constants.LOG_TAG, "no attachment chosen");
            }


            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i,Constants.SEND_EMAIL_REQUEST_CODE);
            } else {
                (Snackbar.make(
                        findViewById(R.id.email_header)
                        ,R.string.sb_text_no_email_client
                        ,Snackbar.LENGTH_INDEFINITE)).show();
            }
        }
    }
}
