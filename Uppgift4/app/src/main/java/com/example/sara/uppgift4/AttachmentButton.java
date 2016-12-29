package com.example.sara.uppgift4;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

class AttachmentButton extends Button {

    Uri attachmentUri = null;

    /**
     * call to superclass constructor.
     * @param context used by XML in some way... I guess.
     */
    public AttachmentButton(Context context) {
        super(context);
    }

    /**
     * Constructor for making the button XML-usable.
     * @param context used by XML in some way...
     * @param attrs used by XML in some way...
     */
    public AttachmentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new AttachmentButtonListener());
    }

    /**
     * Start a file picking activity with startActivityForResult.
     */
    public void pickFile() {

        Intent attachment = new Intent(Intent.ACTION_GET_CONTENT);
        attachment.setType("*/*");
        Intent attChooser = Intent.createChooser(attachment, getContext().getString(R.string.attachment_chooser_header));
        Activity a = getActivity();
        if (a != null) {
            a.startActivityForResult(attChooser, Constants.ATTACHMENT_REQUEST_CODE);
        }

    }

    /**
     * A helper method for getting the relevant activity - if there is one - associated with current
     * context.
     *
     * @return The relevant activity associated with the calling button. May return null if the
     * button is not instantiated correctly.
     */
    @Nullable
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public void addFile(Uri uri) {
        setText(R.string.attachment_button_text_replace);
        attachmentUri = uri;
    }

    private class AttachmentButtonListener implements Button.OnClickListener {

        /**
         * Called when a AttachmentButton has been clicked.
         *
         * Picks a file for attachment in the GUI. If there is already a file attached, it asks for
         * verification.
         *
         * @param v The button that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (attachmentUri != null) {
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
            } else {
                pickFile();
            }

        }
    }


}
