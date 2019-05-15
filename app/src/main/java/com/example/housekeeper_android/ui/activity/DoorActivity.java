package com.example.housekeeper_android.ui.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.housekeeper_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoorActivity extends AppCompatActivity {


    Toolbar door_toolbar;
    Button btnRecord;
    Button btnPlay;

    // TODO: 녹음 기능을 위한 변수 선언
    MediaRecorder mRecorder=null;
    MediaPlayer mPlayer=null;
    String mPath="";
    String todayDate="";
    boolean isRecording = false;
    boolean isPlaying = false;
    ////////////////////////////////////
    private WebView door_streaming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

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

        //툴바 관련
        door_toolbar = (Toolbar) findViewById(R.id.door_toolbar);
        setSupportActionBar(door_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.bar_button_return);

        //웹뷰 관련
        door_streaming=(WebView)findViewById(R.id.door_webview);

        WebSettings mywebSetting=door_streaming.getSettings();//Mobile Web Setting
        mywebSetting.setJavaScriptEnabled(true);//자바스크립트 허용
       // mywebSetting.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        mywebSetting.setLoadWithOverviewMode(true);
        mywebSetting.setUseWideViewPort(true);


        door_streaming.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        door_streaming.loadUrl("http://jsmjsm.iptime.org:8885/?action=stream");


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

    //ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.door_menu, menu);
        return true;
    }

}
