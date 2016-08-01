package com.example.sara.uppgift3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    boolean generateNewPopUp = true;
    MediaRecorder mr = null;
    String myAudio;
    final String LOG_TAG = "uppg3";
    MediaPlayer mp = null;
    Uri videoUri = null;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // jag vet inte
        myAudio = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myAudio.3gp";

        ImageButton ib = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
        ib.setEnabled(false);
        ib = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        ib.setEnabled(false);
        ib = (ImageButton) findViewById(R.id.btn_show_photo);
        ib.setEnabled(false);
        ib = (ImageButton) findViewById(R.id.btn_play_video);
        ib.setEnabled(false);

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(generateNewPopUp){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //builder.setCancelable(true);
            builder.setMessage(getString(R.string.info_about_app));
            builder.setPositiveButton("OK",null);
            //builder.setNegativeButton(R.string.delete_dialog_negative, null);
            builder.show();
            Log.d(LOG_TAG,"popup generated");
            generateNewPopUp = false;
        }
    }
    public void startRecordAudio(View v){

        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setOutputFile(myAudio);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mr.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG,"mr prepared fail");
            e.printStackTrace();
        }

        try {
            mr.start();
         } catch (IllegalStateException e){
            Log.e(LOG_TAG,"illeagal state couldn't start recording");
            e.printStackTrace();
        }

        Log.d(LOG_TAG,"writing to file"+myAudio);
        ImageButton ib = (ImageButton) findViewById(R.id.btn_play_audio);
        ib.setEnabled(false);

        ib = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        ib.setEnabled(true);

        ib = (ImageButton) findViewById(R.id.btn_record_audio);
        ib.setEnabled(false);


    }

    public void stopRecordAudio (View v){
        if (mr != null) {
            mr.stop();
            mr.release();
            mr = null;
        }
        ImageButton ib = (ImageButton) findViewById(R.id.btn_play_audio);
        ib.setEnabled(true);

        ib = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        ib.setEnabled(false);

        ib = (ImageButton) findViewById(R.id.btn_record_audio);
        ib.setEnabled(true);

    }
    
    public void listen (View v) {
        //todo kontrollera att filen finns innan du försöker spela upp den

        mp = new MediaPlayer();

        try {
            mp.setDataSource(myAudio);
            mp.prepare();
            mp.start();
            ImageButton ib = (ImageButton) findViewById(R.id.btn_play_audio);
            ib.setEnabled(false);
            ib = (ImageButton) findViewById(R.id.btn_record_audio);
            ib.setEnabled(false);
            ib = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
            ib.setEnabled(true);
        } catch (IOException e){
            Log.d(LOG_TAG,"the file we tried to read is"+myAudio);
            Log.e(LOG_TAG,"playback failed");
            e.printStackTrace();

        }
    }

    // todo skapa en knapp för att stoppa uppspelningen av ljud.

    //todo se till att onDestroy eller kanske onpause stoppar ljudinspelningen

    public void stopPlayBack (View v){
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
            ImageButton ib = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
            ib.setEnabled(false);
            ib = (ImageButton) findViewById(R.id.btn_play_audio);
            ib.setEnabled(true);
            ib = (ImageButton) findViewById(R.id.btn_record_audio);
            ib.setEnabled(true);
        }

    }

    public void recordVideo (View v){
        Intent takeVideoIntent;
        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(takeVideoIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
        }
        //todo skapa felmeddelande om ifsatsen inte lyckas - snack "failure you have no video in your aPP"
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK){
                    videoUri = data.getData();
                    Log.d(LOG_TAG,"The video Uri captured is"+ videoUri);
                    ImageButton ib = (ImageButton) findViewById(R.id.btn_play_video);
                    ib.setEnabled(true);
                }else{
                    //todo videon blev inte sparad medd
                    Log.d(LOG_TAG, "no video captured");
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK){
                    imgUri = data.getData();
                    Log.d(LOG_TAG,"The image uri captured is"+ imgUri);
                    ImageButton ib = (ImageButton) findViewById(R.id.btn_show_photo);
                    ib.setEnabled(true);
                }else{
                    //todo videon blev inte sparad medd
                    Log.d(LOG_TAG, "no photo captured");
                }
                break;


            default:
                //todo skapa felmedd om konstigt calback efter activity
                Log.e(LOG_TAG, "det är fel calback");
        }
    }

    public void playVideo (View v){
        if (videoUri == null){
            Log.d(LOG_TAG,"There is no video");
        } else {
            VideoView berta;
            berta = new VideoView(this);

            RelativeLayout viewHolder = (RelativeLayout) findViewById(R.id.view_holder);
            viewHolder.removeAllViews();
            viewHolder.addView(berta);

            berta.setVideoURI(videoUri);
            berta.start();

            // the media controller is quite ugly.
            /*MediaController videoControl;
            videoControl= new MediaController(this);
            videoControl.setAnchorView(berta);
            berta.setMediaController(videoControl);
            */
        }

    }

    public void takePhoto(View v) {
        Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager())!= null){
            startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
        }
        //todo skapa felmeddelande om ifsatsen inte lyckas - snack "failure you have no video in your aPP"
    }

    public void showPhoto(View v) {
        if (imgUri == null){
            Log.d(LOG_TAG,"There is no image");
        } else {

            ImageView pelle;
            pelle = new ImageView(this);

            pelle.setImageURI(imgUri);

            // todo centrera viewen i sin parent

            RelativeLayout viewHolder = (RelativeLayout) findViewById(R.id.view_holder);
            viewHolder.removeAllViews();
            viewHolder.addView(pelle);

            //pelle.setVisibility(ImageView.VISIBLE);

        }

    }

}
