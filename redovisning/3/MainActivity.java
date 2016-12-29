package com.example.sara.uppgift3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.IOException;

/**
 * Main activity for the media diary.
 */
public class MainActivity extends AppCompatActivity {
    boolean generateWelcomePopUp = true;
    MediaRecorder mr = null;
    String pathToTheAudioFile;
    final String LOG_TAG = "MediaMemory";
    MediaPlayer mp = null;
    Uri videoUri = null;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    Bitmap imgThumb = null;

    /**
     * The standard activity lifecycle onCreate.
     * <p>
     * Creates a path to the audio file and disables some controls.
     *
     * @param savedInstanceState not in use. only included for compatibility reasons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pathToTheAudioFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pathToTheAudioFile.3gp";


        ImageButton ib_play_video = (ImageButton) findViewById(R.id.btn_play_video);
        ImageButton ib_show_photo = (ImageButton) findViewById(R.id.btn_show_photo);
        ImageButton ib_stop_audio_rec = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        ImageButton ib_stop_audio = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
        if (ib_play_video != null && ib_stop_audio_rec != null && ib_show_photo != null && ib_stop_audio != null) {
            ib_play_video.setEnabled(false);
            ib_stop_audio_rec.setEnabled(false);
            ib_show_photo.setEnabled(false);
            ib_stop_audio.setEnabled(false);
        } else {
            Log.e(LOG_TAG, "null pointer error - couldnt find all the control buttons.");
        }
    }

    /**
     * Normal onStart method.
     * Displays a welcome message if it is the first time the app is opened.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (generateWelcomePopUp) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getString(R.string.welcome_popup_text));
            builder.setPositiveButton(getString(R.string.welcome_popup_button), null);
            builder.show();
            Log.d(LOG_TAG, "popup generated");
            generateWelcomePopUp = false;
        }
    }

    /**
     * onStop method. Makes sure the Media player is released properly.
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopAudio(null);
        stopRecordAudio(null);
    }

    /**
     * Start the audio recording and change the status of what audio buttons one can click.
     *
     * @param v the view that was clicked. This is to allow of setting click listener via xml. The
     *          parameter is not used.
     */
    public void startRecordAudio(View v) {

        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setOutputFile(pathToTheAudioFile);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mr.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "preparation of media recorder failed");
            e.printStackTrace();
        }

        try {
            mr.start();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "media recorder illeagal state - couldn't start audio recording");
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "writing to file" + pathToTheAudioFile);

        ImageButton ib_play_audio = (ImageButton) findViewById(R.id.btn_play_audio);
        ImageButton ib_start_audio_rec = (ImageButton) findViewById(R.id.btn_record_audio);
        ImageButton ib_stop_audio_rec = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        if (ib_play_audio != null && ib_stop_audio_rec != null && ib_start_audio_rec != null) {
            ib_play_audio.setEnabled(false);
            ib_stop_audio_rec.setEnabled(true);
            ib_start_audio_rec.setEnabled(false);
        } else {
            Log.e(LOG_TAG, "null pointer error - couldnt find all the audio control buttons.");
        }

    }

    /**
     * Stop recording audio.
     *
     * @param v view parameter to allow xml click registration on buttons.
     */
    public void stopRecordAudio(@Nullable View v) {
        if (mr != null) {
            mr.stop();
            mr.reset(); //this flushes all events on the media recorder. not neccessary unless you get fatals. http://stackoverflow.com/questions/11863488/trying-to-record-voice-in-android-but-getting-error
            mr.release();
            mr = null;
        }

        ImageButton ib_play_audio = (ImageButton) findViewById(R.id.btn_play_audio);
        ImageButton ib_start_audio_rec = (ImageButton) findViewById(R.id.btn_record_audio);
        ImageButton ib_stop_audio_rec = (ImageButton) findViewById(R.id.btn_stop_audio_recording);
        if (ib_play_audio != null && ib_stop_audio_rec != null && ib_start_audio_rec != null) {
            ib_play_audio.setEnabled(true);
            ib_stop_audio_rec.setEnabled(false);
            ib_start_audio_rec.setEnabled(true);
        } else {
            Log.e(LOG_TAG, "null pointer error - couldnt find all the audio control buttons.");
        }

    }

    /**
     * Playback of the previously recorded audio file. If the file is not recorded yet or any other
     * playback error occurs, we get trouble. Such errors are handled to some extent, but there are
     * probably non covered edge cases I've missed...
     *
     * @param v a view parameter for xml click listener registration. not used.
     */
    public void playAudio(@Nullable View v) {

        mp = new MediaPlayer();

        try {
            mp.setDataSource(pathToTheAudioFile);
        } catch (IOException e) {
            Log.d(LOG_TAG, "the file we tried to read is" + pathToTheAudioFile);
            Log.e(LOG_TAG, "failed to load the audio - does the file exist?");
            mp.reset();
            mp.release();
            mp = null;
            return;
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "the file we tried to read is" + pathToTheAudioFile);
            Log.e(LOG_TAG, "failed to prepare the media player - is there correct read permissions on the file?");
            mp.reset();
            mp.release();
            mp = null;
            return;
        }
        mp.start();


        ImageButton ib_play_audio = (ImageButton) findViewById(R.id.btn_play_audio);
        ImageButton ib_start_audio_rec = (ImageButton) findViewById(R.id.btn_record_audio);
        ImageButton ib_stop_audio = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
        if (ib_play_audio != null && ib_stop_audio != null && ib_start_audio_rec != null) {
            ib_play_audio.setEnabled(false);
            ib_stop_audio.setEnabled(true);
            ib_start_audio_rec.setEnabled(false);
        } else {
            Log.e(LOG_TAG, "null pointer error - couldnt find all the audio control buttons.");
        }
    }

    /**
     * Stop audio playback.
     *
     * @param v a view parameter to allow xml click listener registration. not in use.
     */
    public void stopAudio(@Nullable View v) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

            ImageButton ib_play_audio = (ImageButton) findViewById(R.id.btn_play_audio);
            ImageButton ib_start_audio_rec = (ImageButton) findViewById(R.id.btn_record_audio);
            ImageButton ib_stop_audio = (ImageButton) findViewById(R.id.btn_stop_audio_playback);
            if (ib_play_audio != null && ib_stop_audio != null && ib_start_audio_rec != null) {
                ib_play_audio.setEnabled(true);
                ib_stop_audio.setEnabled(false);
                ib_start_audio_rec.setEnabled(true);
            } else {
                Log.e(LOG_TAG, "null pointer error - couldn't find all the audio control buttons. " +
                        "This is okay if the function is called from the onStop method. :)");
            }
        }

    }

    /**
     * Ask the android system to record a video via an intent. If there is no video recording app on
     * the phone, no error message is presented.
     * <p>
     * The video taken is stored to the phone.
     *
     * @param v a view parameter to allow xml click listener registration.
     */
    public void recordVideo(View v) {
        Intent takeVideoIntent;
        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            Log.v(LOG_TAG, "starting video capture activity");
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }

    /**
     * Ask the android system to take a photo via an intent. If there is no photo app on
     * the phone the method fails quietly.
     * <p>
     * The taken photo is not stored to the phone, it's saved in a cash and will thus disappear when the app is closed.
     *
     * @param v a view parameter to allow xml click listener registration.
     */
    public void takePhoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.v(LOG_TAG, "Starting photo capture activity");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Handling of completed video or photo capture. If the activity of photo/video failed, such as
     * the user pressed the "back" button in the video/photo app, this is not handled so well...
     *
     * @param requestCode The requested activity that has now completed. Was passed with the intent
     *                    when the activity was started.
     * @param resultCode  Standard android result code.
     * @param data        the returning intent from the activity that was completed. carries data of
     *                    interest
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    videoUri = data.getData();
                    Log.d(LOG_TAG, "The video Uri captured is" + videoUri);
                    ImageButton ib = (ImageButton) findViewById(R.id.btn_play_video);
                    if (ib != null) {
                        ib.setEnabled(true);
                    }
                } else {
                    Log.d(LOG_TAG, "no video captured");
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    imgThumb = (Bitmap) data.getExtras().get("data");
                    Log.d(LOG_TAG, "The image bitmap captured is" + imgThumb);
                    ImageButton ib = (ImageButton) findViewById(R.id.btn_show_photo);
                    if (ib != null) {
                        ib.setEnabled(true);
                    }
                } else {
                    Log.d(LOG_TAG, "no photo captured");
                }
                break;

            default:
                Log.e(LOG_TAG, "invalid request code sent to onActivityResult method");
        }
    }

    /**
     * Mounting a new videoView inside a viewHolder and playing the video latest recorded by this
     * app.
     *
     * @param v a view parameter for xml onClick listener registration. not used.
     */
    public void playVideo(@Nullable View v) {
        if (videoUri == null) {
            Log.d(LOG_TAG, "There is no video to play.");
        } else {
            VideoView videoView = new VideoView(this);
            RelativeLayout viewHolder = (RelativeLayout) findViewById(R.id.view_holder);
            if (viewHolder != null) {
                viewHolder.removeAllViews();
                viewHolder.addView(videoView);
                videoView.setVideoURI(videoUri);
                videoView.start();
                Log.v(LOG_TAG, "Playing video");
            }
        }

    }


    /**
     * Showing the photo last taken with this app.
     *
     * @param v a view parameter for xml onClick listener registration. not used.
     */
    public void showPhoto(@Nullable View v) {
        if (imgThumb == null) {
            Log.d(LOG_TAG, "There is no image");
        } else {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(imgThumb);
            RelativeLayout viewHolder = (RelativeLayout) findViewById(R.id.view_holder);
            if (viewHolder != null) {
                viewHolder.removeAllViews();
                viewHolder.addView(imageView);
                imageView.setVisibility(ImageView.VISIBLE);
                Log.v(LOG_TAG, "displaying the photo");
            }
        }
    }

}

