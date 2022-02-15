package com.example.audioplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button up,down,start;
    float speed = 1f;

    // User input mp3 file url in this text box. Or display user selected mp3 file name.
    private TextView audioFilePathEditor,duration,title,haay_text;
    // Click this button to let user select mp3 file.
    private ImageButton browseAudioFileButton;
    // Start play audio button.
    private ImageButton startButton;
    private ImageView girl;
    private Button one_x_button,two_x_button,zero_five_x_button,zero_seven_x_button,one_five_x_button;

    int currPlayPosition;
    // Pause playing audio button.
    private ImageButton pauseButton;
    // Stop playing audio button.
    private ImageButton stopButton;
    // Show played audio progress.
    private ProgressBar playAudioProgress;
    // Used when user select audio file.
    private static final int REQUEST_CODE_SELECT_AUDIO_FILE = 1;
    // Used when user require android READ_EXTERNAL_PERMISSION.
    private static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 2;
    // Used when update audio progress thread send message to progress bar handler.
    private static final int UPDATE_AUDIO_PROGRESS_BAR = 3;
    // Used to control audio (start, pause , stop etc).
    private MediaPlayer audioPlayer = null;
    // Save user selected or inputted audio file unique resource identifier.
    private Uri audioFileUri = null;
    // Used to distinguish log data.
    public static final String TAG_PLAY_AUDIO = "PLAY_AUDIO";
    // Wait update audio progress thread sent message, then update audio play progress.
    private Handler audioProgressHandler = null;
    // The thread that send message to audio progress handler to update progress every one second.
    private Thread updateAudioPalyerProgressThread = null;
    // Record whether audio is playing or not.
    private boolean audioIsPlaying = false;

    AlphaAnimation startButtonAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation stopButtonAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation pauseButtonAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation zeroFiveXAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation zeroSevenXAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation oneXAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation oneFiveXAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation twoXAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation searchButtonAnimation = new AlphaAnimation(0.1f, 1f);
    AlphaAnimation haayAnimation = new AlphaAnimation(1f, 0.1f);


    TranslateAnimation animateLogo = new TranslateAnimation(0.0f, 0.0f, 1200.0f, 0.0f);
    TranslateAnimation animateTop = new TranslateAnimation(-500f, 0.0f, 0.0f, 0.0f);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        one_x_button = findViewById(R.id.one_x);
        two_x_button = findViewById(R.id.two_x);
        zero_five_x_button = findViewById(R.id.zero_five_x);
        zero_seven_x_button = findViewById(R.id.zero_seven_x);
        one_five_x_button = findViewById(R.id.one_five_x);
        girl = findViewById(R.id.girl);

        startButton = findViewById(R.id.play_audio_start_button);
        pauseButton = findViewById(R.id.play_audio_pause_button);
        stopButton = findViewById(R.id.play_audio_stop_button);
        playAudioProgress = findViewById(R.id.play_audio_progressbar);

        duration = findViewById(R.id.duration);
        title = findViewById(R.id.title);
        haay_text = findViewById(R.id.haay_text);

        startButton.setEnabled(false);


        zero_five_x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(zeroFiveXAnimation);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PlaybackParams params = audioPlayer.getPlaybackParams();
                    params.setSpeed(1f);
                    audioPlayer.setPlaybackParams(params);

                }
            }
        });

        zero_seven_x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(zeroSevenXAnimation);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PlaybackParams params = audioPlayer.getPlaybackParams();
                    params.setSpeed(1.25f);
                    audioPlayer.setPlaybackParams(params);

                }
            }
        });
        one_x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(oneXAnimation);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PlaybackParams params = audioPlayer.getPlaybackParams();
                    params.setSpeed(1.5f);
                    audioPlayer.setPlaybackParams(params);

                }
            }
        });

        one_five_x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(oneFiveXAnimation);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PlaybackParams params = audioPlayer.getPlaybackParams();
                    params.setSpeed(1.75f);
                    audioPlayer.setPlaybackParams(params);

                }
            }
        });

        two_x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(twoXAnimation);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PlaybackParams params = audioPlayer.getPlaybackParams();
                    params.setSpeed(2f);
                    audioPlayer.setPlaybackParams(params);

                }
            }
        });
        /* Initialize audio progress handler. */
        if(audioProgressHandler==null) {
            audioProgressHandler = new Handler() {
                @SuppressLint("HandlerLeak")
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                        if(audioPlayer!=null) {
                            // Get current play time.



                            currPlayPosition = audioPlayer.getCurrentPosition();
                            // Get total play time.
                            int totalTime = audioPlayer.getDuration();

                            long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);

                            playAudioProgress.setMax((int)seconds);




//                            Log.i("sec", String.valueOf(DocumentFile.fromSingleUri(getApplicationContext(), audioFileUri).getName()));
//                            int seconds=totalTime/1000;

                            // Calculate the percentage.
                            int currProgress = (currPlayPosition * 1000) / totalTime;

                            int second = (int) (seconds % 60);
                            int minute = (int) seconds / 60;
                            if (minute >= 60) {
                                int hour = minute / 60;
                                minute %= 60;
                                duration.setText( hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second));;
                            }
                            duration.setText(minute + ":" + (second < 10 ? "0" + second : second));
                            if(currProgress % 2 == 1) haay_text.startAnimation(haayAnimation);

                            // Update progressbar.
                            playAudioProgress.setProgress(currProgress);
                        }
                    }
                }
            };
        }

        /* When user input key up in this text editor. */
        audioFilePathEditor = findViewById(R.id.play_audio_file_path_editor);
        audioFilePathEditor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                int action = keyEvent.getAction();
                if(action == KeyEvent.ACTION_UP) {
                    String text = audioFilePathEditor.getText().toString();
                    if (text.length() > 0) {
                        startButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        startButton.setAlpha(1f);
                        pauseButton.setAlpha(0.4f);
                        stopButton.setAlpha(0.4f);
                    } else {
                        startButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);

                        startButton.setAlpha(0.4f);
                        pauseButton.setAlpha(0.4f);
                        stopButton.setAlpha(0.4f);
                    }
                }
                return false;
            }
        });
        /* Click this button to popup select audio file component. */
        browseAudioFileButton = findViewById(R.id.play_audio_browse_audio_file_button);
        browseAudioFileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(searchButtonAnimation);
                // Require read external storage permission from user.
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(MainActivity.this, requirePermission, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                }else {
                    selectAudioFile();
                }
            }
        });

        // When start button is clicked.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    view.startAnimation(startButtonAnimation);
                    haay_text.setText("Aferin çalışıyon Şuleee");
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    one_x_button.setEnabled(true);
                    two_x_button.setEnabled(true);
                    zero_five_x_button.setEnabled(true);
                    zero_seven_x_button.setEnabled(true);
                    one_five_x_button.setEnabled(true);

                    startButton.setAlpha(1f);
                    pauseButton.setAlpha(1f);
                    stopButton.setAlpha(1f);
                    one_x_button.setAlpha(1f);
                    two_x_button.setAlpha(1f);
                    zero_five_x_button.setAlpha(1f);
                    zero_seven_x_button.setAlpha(1f);
                    one_five_x_button.setAlpha(1f);

                    String audioFilePath = audioFilePathEditor.getText().toString();
                    if (!TextUtils.isEmpty(audioFilePath)) {
                        stopCurrentPlayAudio();
                        initAudioPlayer();
                        audioPlayer.start();
                        audioIsPlaying = true;

                        // Display progressbar.
                        playAudioProgress.setVisibility(ProgressBar.VISIBLE);
                        if (updateAudioPalyerProgressThread == null) {
                            // Create the thread.
                            updateAudioPalyerProgressThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        while (audioIsPlaying) {
                                            if (audioProgressHandler != null) {
                                                // Send update audio player progress message to main thread message queue.
                                                Message msg = new Message();
                                                msg.what = UPDATE_AUDIO_PROGRESS_BAR;
                                                audioProgressHandler.sendMessage(msg);
                                                Thread.sleep(1000);
                                            }
                                        }
                                    } catch (InterruptedException ex) {
                                        Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
                                    }
                                }
                            };
                            updateAudioPalyerProgressThread.start();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Bu dosya oynatılamıyo ıggghhhh", Toast.LENGTH_LONG).show();
                    }
            }
        });

        /* When pause button is clicked. */
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(pauseButtonAnimation);

                if(audioIsPlaying)
                {
                    audioPlayer.pause();
                    startButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    startButton.setAlpha(1f);
                    pauseButton.setAlpha(0.4f);
                    stopButton.setAlpha(1f);

                    audioIsPlaying = false;
                    updateAudioPalyerProgressThread = null;
                }
            }
        });
        /* When stop button is clicked. */
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(stopButtonAnimation);
                if(audioIsPlaying)
                {
                    audioPlayer.stop();
                    audioPlayer.release();
                    audioPlayer = null;
                    startButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);

                    startButton.setAlpha(1f);
                    pauseButton.setAlpha(0.4f);
                    stopButton.setAlpha(0.4f);
                    updateAudioPalyerProgressThread = null;
                    playAudioProgress.setProgress(0);
                    audioIsPlaying = false;
                }
            }
        });

        animations();

    }

    /* Initialize media player. */
    private void initAudioPlayer()
    {
        try {
            if(audioPlayer == null)
            {
                audioPlayer = new MediaPlayer();
                String audioFilePath = audioFilePathEditor.getText().toString().trim();
                Log.d(TAG_PLAY_AUDIO, audioFilePath);
                if(audioFilePath.toLowerCase().startsWith("http://"))
                {
                    // Web audio from a url is stream music.
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // Play audio from a url.
                    audioPlayer.setDataSource(audioFilePath);
                }else
                {
                    if(audioFileUri != null)
                    {
                        // Play audio from selected local file.
                        audioPlayer.setDataSource(getApplicationContext(), audioFileUri);
                    }
                }
                audioPlayer.prepare();
            }
        }catch(IOException ex)
        {
            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
        }
    }
    private String getRealPathFromURI(Uri uri) {
        File myFile = new File(uri.getPath().toString());
        String s = myFile.getAbsolutePath();
        return s;
    }
    /* This method start get content activity to let user select audio file from local directory.*/
    private void selectAudioFile()
    {
        // Create an intent with ACTION_GET_CONTENT.
        Intent selectAudioIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // Show audio in the content browser.
        // Set selectAudioIntent.setType("*/*") to select all data
        // Intent for this action must set content type, otherwise android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT } will be thrown
        selectAudioIntent.setType("audio/*");
        // Start the activity.
        startActivityForResult(selectAudioIntent, REQUEST_CODE_SELECT_AUDIO_FILE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_AUDIO_FILE) {
            if (resultCode == RESULT_OK) {
                // To make example simple and clear, we only choose audio file from
                // local file, this is easy to get audio file real local path.
                // If you want to get audio file real local path from a audio content provider
                // Please read another article.
                audioFileUri = data.getData();
                String audioFileName = audioFileUri.getLastPathSegment();
                audioFilePathEditor.setText("Seçilen Dosya: " + audioFileName);
                title.setText(String.valueOf(DocumentFile.fromSingleUri(getApplicationContext(), audioFileUri).getName()));
                initAudioPlayer();
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);

                startButton.setAlpha(1f);
                pauseButton.setAlpha(0.4f);
                stopButton.setAlpha(0.4f);
            }
        }
    }
    /* Stop current play audio before play another. */
    private void stopCurrentPlayAudio()
    {
        if(audioPlayer!=null && audioPlayer.isPlaying())
        {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }
    /* This method will be called after user choose grant read external storage permission or not. */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_PERMISSION) {
            if (grantResults.length > 0) {
                int grantResult = grantResults[0];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // If user grant the permission then open browser let user select audio file.
                    selectAudioFile();
                } else {
                    Toast.makeText(getApplicationContext(), "Uygulama için izin vermen lazım suleee", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        if(audioPlayer!=null)
        {
            if(audioPlayer.isPlaying())
            {
                audioPlayer.stop();
            }
            audioPlayer.release();
            audioPlayer = null;
        }
        super.onDestroy();
    }

    public void animations() {
        try {
            animateLogo.setDuration(1550); // animation duration
            animateLogo.setFillAfter(false);
            animateTop.setDuration(1550); // animation duration
            animateTop.setFillAfter(false);

            girl.startAnimation(animateLogo);
            startButton.startAnimation(animateLogo);
            stopButton.startAnimation(animateLogo);
            pauseButton.startAnimation(animateLogo);
            one_five_x_button.startAnimation(animateLogo);
            one_x_button.startAnimation(animateLogo);
            zero_seven_x_button.startAnimation(animateLogo);
            zero_five_x_button.startAnimation(animateLogo);
            two_x_button.startAnimation(animateLogo);
            browseAudioFileButton.startAnimation(animateLogo);
            playAudioProgress.startAnimation(animateLogo);
            title.startAnimation(animateLogo);
            haay_text.startAnimation(animateLogo);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            haayAnimation.setDuration(1000);
            haayAnimation.setStartOffset(50);
            haayAnimation.setFillAfter(false);

            startButtonAnimation.setDuration(500);
            startButtonAnimation.setStartOffset(50);
            startButtonAnimation.setFillAfter(false);

            stopButtonAnimation.setDuration(500);
            stopButtonAnimation.setStartOffset(50);
            stopButtonAnimation.setFillAfter(false);

            pauseButtonAnimation.setDuration(500);
            pauseButtonAnimation.setStartOffset(50);
            pauseButtonAnimation.setFillAfter(false);

            zeroFiveXAnimation.setDuration(500);
            zeroFiveXAnimation.setStartOffset(50);
            zeroFiveXAnimation.setFillAfter(false);

            zeroSevenXAnimation.setDuration(500);
            zeroSevenXAnimation.setStartOffset(50);
            zeroSevenXAnimation.setFillAfter(false);

            oneXAnimation.setDuration(500);
            oneXAnimation.setStartOffset(50);
            oneXAnimation.setFillAfter(false);

            oneFiveXAnimation.setDuration(500);
            oneFiveXAnimation.setStartOffset(50);
            oneFiveXAnimation.setFillAfter(false);

            twoXAnimation.setDuration(500);
            twoXAnimation.setStartOffset(50);
            twoXAnimation.setFillAfter(false);

            searchButtonAnimation.setDuration(500);
            searchButtonAnimation.setStartOffset(50);
            searchButtonAnimation.setFillAfter(false);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}