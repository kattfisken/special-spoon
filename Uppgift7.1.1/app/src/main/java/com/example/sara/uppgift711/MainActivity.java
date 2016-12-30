package com.example.sara.uppgift711;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Starts the main activity of the app. In the activity it's possible to write something that you
 * want to copy and to paste it.
 */
public class MainActivity extends AppCompatActivity {
    ClipboardManager clipBoard = null;

    /**
     * Draws GUI, creates a clip board, registers a copy listener ad paste listener for the relevant
     * buttons.
     *
     * @param savedInstanceState not used.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("uppgift7.1.1", "oncreate has started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        Button copy_button = (Button) findViewById(R.id.CopyButton);
        CopyListener cp = new CopyListener();


        if (copy_button != null) {
            copy_button.setOnClickListener(cp);
        }

        Button paste_button = (Button) findViewById(R.id.pasteButton);


        if (paste_button != null) {
            paste_button.setOnClickListener(cp);
        }
        Log.d("uppgift 7.1.1", "oncreate is finished");
    }

    /**
     * Implementation of onClickListener for the copy/paste button.
     */

    class CopyListener implements View.OnClickListener {
        /**
         * If copy button is pressed in the GUI, MainActivity.copyAction is activated,
         * if paste button is pressed int the GUI, MainActivity.pasteAction is activated.
         *
         * @param theViewThatWasPressed the button that was pressed so that we can see if it's the
         *                              copy or paste button.
         */
        @Override
        public void onClick(View theViewThatWasPressed) {
            if (theViewThatWasPressed.getId() == R.id.CopyButton) {
                copyAction();
            } else if (theViewThatWasPressed.getId() == R.id.pasteButton) {
                pasteAction();

            }


        }
    }

    /**
     * Takes first item in clipboard and puts in the last position of items in paste window
     */
    private void pasteAction() {
        Log.d("uppgift 7.1.1", "paste action has been called");
        ClipData clip = clipBoard.getPrimaryClip();

        // if it is a multiple selection clip, we won't handle that
        ClipData.Item item = clip.getItemAt(0);

        //if it's not a string, we depend on the clipboards ability to represent it as a string
        CharSequence text = item.getText();

        EditText pasteView = (EditText) findViewById(R.id.pasteView);


        Editable pasteViewContent = null;
        if (pasteView != null) {
            pasteViewContent = pasteView.getText();
        }
        if (pasteViewContent != null) {
            pasteViewContent.append(text);
            Toast.makeText(MainActivity.this, "text pasted from clipboard", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Log.e("uppgift 7.1.1", "is the GUI broken?");
        }
    }

    /**
     * Copies the text from the copy text box and puts it ion the clip board.
     */
    private void copyAction() {
        Log.d("uppgift 7.1.1", "copy action has been called");

        EditText copyView = (EditText) findViewById(R.id.copyView);

        String textToCopy = null;
        if (copyView != null) {
            textToCopy = copyView.getText().toString();
        } else {
            Log.e("uppgift7.1.1", "failed to acquire editText with the message to copy");
        }

        if (textToCopy != null) {
            if (textToCopy.length() != 0) {
                ClipData clip = ClipData.newPlainText("copied text from copy and paste app"
                        , textToCopy);
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
