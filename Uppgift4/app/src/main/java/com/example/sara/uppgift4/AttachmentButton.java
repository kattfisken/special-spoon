package com.example.sara.uppgift4;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AttachmentButton extends Button {

    Uri attachmentUri = null;

    public AttachmentButton(Context context) {
        super(context);
    }

    public AttachmentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText(R.string.attatchement_button_text);
        setOnClickListener(new AttachmentButtonListener());
    }


    public void pickFile() {

            Intent attachment = new Intent(Intent.ACTION_GET_CONTENT);
            attachment.setType("*/*");
            Intent attChooser = Intent.createChooser(attachment, getContext().getString(R.string.attachment_chooser_header));
            Activity a = getActivity();
            a.startActivityForResult(attChooser,Constants.ATTACHMENT_REQUEST_CODE);

    }

    /**
     * A helper method for getting the relevant activity - if there is one - associated with current context
     * @return The relevant activity associated with the calling button. May return null if the button is not instanciated correctly
     */
    @Nullable
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void addFile(Uri uri){
        setText(R.string.attachment_button_text_replace);
        attachmentUri = uri;
    }

    private class AttachmentButtonListener implements Button.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if(attachmentUri != null){
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Do you really want to replace the current attachment?");
                ab.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickFile();
                    }
                });
                ab.setNegativeButton("No thanks!", null);
                ab.show();
            }else{
                pickFile();
            }

        }
    }



}
