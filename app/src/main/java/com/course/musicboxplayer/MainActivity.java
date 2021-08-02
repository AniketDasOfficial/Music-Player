
package com.course.musicboxplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private ImageView imageView;
    private TextView textView,leftTime, rightTime;
    private SeekBar seekBar;
    private Button prevButton, playButton, nextButton;
    private Thread thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        textView.setTextColor(Color.rgb(0,255,255));
        setUpUI();

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser == true){

                    mediaPlayer.seekTo(progress);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
//                Date date = 05:30:00;
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                System.out.println(mediaPlayer.getCurrentPosition());
                System.out.println(mediaPlayer.getDuration());
//
//                System.out.println(dateFormat.format(new Date(currentPos)));
                System.out.println(dateFormat.format(new Date(duration - currentPos)));

                String[] s = String.valueOf(dateFormat.format(new Date(currentPos))).split(":");
                int a = Integer.parseInt(s[0]) - 30;

                String str = a + ":" + s[1];
                leftTime.setText(str);

//---------------------------------------------------------------------------------------------------------------------------------------

                String[] s1 = String.valueOf(dateFormat.format(new Date(duration-currentPos ))).split(":");
                int a1 = Integer.parseInt(s1[0]) - 30;

                String str1 = a1 + ":" + s1[1];

                rightTime.setText(str1);

//                System.out.println("left time: " + str);
                System.out.println("right time: " + s1[0]);

//                System.out.println(toString( dateFormat.format("30:00") - dateFormat.format(new Date(currentPos))));
                //System.out.println("duration: " + duration);

//                System.out.println(new Date(currentPos));
//                System.out.println(new Date(duration-currentPos));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void setUpUI(){

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.cwfe);

//        imageView = (ImageView) findViewById(R.id.albumViewID);
        leftTime = (TextView) findViewById(R.id.leftTimeID);
        rightTime = (TextView) findViewById(R.id.rightTimeID);
        seekBar = (SeekBar) findViewById(R.id.seekBarID);
        prevButton = (Button) findViewById(R.id.prevButtonID);
        playButton = (Button) findViewById(R.id.playButtonID);
        nextButton = (Button) findViewById(R.id.nextButtonID);

        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.prevButtonID :
                backButton();
                break;

            case R.id.playButtonID:
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }else{
                    startMusic();
                }

                break;

            case R.id.nextButtonID:
                nextButton();
                break;

        }

    }

    public void pauseMusic(){

        if(mediaPlayer != null) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        }

    }

    public void startMusic(){

        if(mediaPlayer != null){
            mediaPlayer.start();
            updateThread();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }

    }

    public void backButton(){

        if(mediaPlayer.isPlaying())
            mediaPlayer.seekTo(0);

    }

    public void nextButton(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
            playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    public void updateThread(){

        thread = new Thread(){

            @Override
            public void run() {

                try{

                    while (mediaPlayer != null && mediaPlayer.isPlaying()){


                        Thread.sleep( 50 );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int newPosition = mediaPlayer.getCurrentPosition();
                                int newMax = mediaPlayer.getDuration();

                                seekBar.setMax(newMax);
                                seekBar.setProgress(newPosition);

                                //Update Time in text

//                                leftTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss") .format(new Date(mediaPlayer.getCurrentPosition()))));
//                                rightTime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition())));


//                                int start = mediaPlayer.getCurrentPosition();
//                                int last = mediaPlayer.getDuration();
//                                int a = last - start;
//
//                                System.out.println(a);

                                //System.out.println();

                            }
                        });

                    }

                }catch (InterruptedException e){

                    e.printStackTrace();

                }

            }
        };
        thread.start();


    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!= null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer  = null;
        }

        thread.interrupt();
        thread = null;
        super.onDestroy();
    }
}