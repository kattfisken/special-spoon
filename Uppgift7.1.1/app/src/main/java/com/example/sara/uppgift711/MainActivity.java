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

public class MainActivity extends AppCompatActivity {
    ClipboardManager clipBoard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("uppgift7.1.1","oncreate har startat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        Button copy_button = (Button) findViewById(R.id.CopyButton);
        CopyListener cp = new CopyListener();

        //noinspection ConstantConditions
        copy_button.setOnClickListener(cp);

        Button paste_button = (Button)findViewById(R.id.pasteButton);

        //noinspection ConstantConditions
        paste_button.setOnClickListener(cp);
        Log.d("uppgift 7.1.1","on create är färdig");
    }

    class CopyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String s = null;
            if (v.getId()== R.id.CopyButton){
                s = "copy";
                copyAction();
            }else if(v.getId()== R.id.pasteButton){
                s = "paste";
                pasteAction();


            }

            Log.d("uppgift 7.1.1","nu har någon klickat"+s);

        }
    }

    private void pasteAction() {
        Log.d("uppgift 7.1.1", "nu har paste action blivit anropad");
        ClipData clip = clipBoard.getPrimaryClip();
        ClipData.Item item = clip.getItemAt(0); //if it is a multiple selection clip, we won't handle that
        CharSequence text = item.getText(); //if it's not a string, we depend on the clipboards ability to represent it as a string
        EditText pasteView = (EditText)findViewById(R.id.pasteView);
        //noinspection ConstantConditions
        Editable pasteViewContent = pasteView.getText();
        pasteViewContent.append(text);
        Toast.makeText(MainActivity.this, "text pasted from clipboard", Toast.LENGTH_SHORT).show();
    }



    private void copyAction() {
        Log.d("uppgift 7.1.1","nu har copy action metoden blivit anropad");
        EditText copyView =(EditText)findViewById(R.id.copyView);
        Editable textToCopy_asEditable = null;
        if (copyView != null) {
            textToCopy_asEditable = copyView.getText();
        } else{
            Log.e("uppgift7.1.1", "failed to aquire editText with the message to copy");
        }
        String textToCopy = null;
        if (textToCopy_asEditable != null) {
            textToCopy = textToCopy_asEditable.toString();
        }


        if (textToCopy != null) {
            if( textToCopy.length() != 0){
                ClipData clip;
                clip = ClipData.newPlainText("copied text from copy and paste app",textToCopy);
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
