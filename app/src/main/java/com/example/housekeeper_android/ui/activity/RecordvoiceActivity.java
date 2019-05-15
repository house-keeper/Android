package com.example.housekeeper_android.ui.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.housekeeper_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordvoiceActivity extends AppCompatActivity {

    Button btnRecord;
    Button btnPlay;
    MediaRecorder mRecorder=null;
    MediaPlayer mPlayer=null;
    String mPath="";
    String todayDate="";
    boolean isRecording = false;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvoice);

        btnRecord = (Button)findViewById(R.id.btnRecord);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        mRecorder = new MediaRecorder();


        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording == false) {
                    initAudioRecorder();
                    mRecorder.start();

                    isRecording = true;
                    btnRecord.setText("Stop Recording");
                } else {
                    mRecorder.stop();

                    isRecording = false;
                    btnRecord.setText("Start Recording");
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying == false) {
                    try {
                        Log.d("RECORD_TEST","playing file path is "+mPath);
                        mPlayer.reset();
                        mPlayer.setDataSource(mPath);
                        mPlayer.prepare();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();

                    isPlaying = true;
                    btnPlay.setText("Stop Playing");
                }
                else {
                    mPlayer.stop();

                    isPlaying = false;
                    btnPlay.setText("Start Playing");
                }
            }
        });

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                btnPlay.setText("Start Playing");
            }
        });
    }

    void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Date today = new Date();
        System.out.println(today);

        SimpleDateFormat date = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("hhmmss");

        todayDate = date.format(today)+time.format(today);

        mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+todayDate+".mp4";
        Log.d("RECORD_TEST", "file path is " + mPath);
        mRecorder.setOutputFile(mPath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
