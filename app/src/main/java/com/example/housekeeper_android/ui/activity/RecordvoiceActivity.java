package com.example.housekeeper_android.ui.activity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.NetworkService;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;

import java.io.File;
import java.io.FileInputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordvoiceActivity extends AppCompatActivity {

    Button btnRecord;
    Button btnPlay;
    Button btnSave;
    EditText txtFileName;
    MediaRecorder mRecorder=null;
    MediaPlayer mPlayer=null;
    String fileName=""; //
    String mPath="";
    String todayDate="";
    boolean isRecording = false;
    boolean isPlaying = false;

    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvoice);

        networkService = ApplicationController.getInstance().getNetworkService();

        btnRecord = (Button)findViewById(R.id.btnRecord);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnSave = (Button)findViewById(R.id.btnSave);
        txtFileName = (EditText)findViewById(R.id.txtFileName);
        mRecorder = new MediaRecorder();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.fromFile(new File(mPath));
                File file = new File(String.valueOf(uri));
               // File file = new File(mPath);
                Log.d("PATH_TEST",String.valueOf(uri));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file",file.getName(),requestFile);

                Call<PostRecordFileResponse> postRecordFileResponseCall = networkService.postRecordFile(uploadFile);
                postRecordFileResponseCall.enqueue(new Callback<PostRecordFileResponse>() {
                    @Override
                    public void onResponse(Call<PostRecordFileResponse> call, Response<PostRecordFileResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<PostRecordFileResponse> call, Throwable t) {

                    }
                });
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording == false) {
                    initAudioRecorder();
                    mRecorder.start();

                    isRecording = true;
                    btnRecord.setText("멈추기");
                } else {
                    mRecorder.stop();

                    isRecording = false;
                    btnRecord.setText("녹음하기");
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
                    btnPlay.setText("멈춤");
                }
                else {
                    mPlayer.stop();

                    isPlaying = false;
                    btnPlay.setText("재생");
                }
            }
        });

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                btnPlay.setText("재생");
            }
        });
    }

    void initAudioRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        fileName = txtFileName.getText().toString();

        /* 오늘의 날짜 불러오기 & 파일명 지정
        Date today = new Date();
        System.out.println(today);
        SimpleDateFormat date = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("hhmmss");
        todayDate = date.format(today)+time.format(today);
        */


       mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+fileName+".mp4";
      //  mPath = Context.getFilesDir().getAbsolutePath()
        Log.d("RECORD_TEST", "file path is " + mPath);
        mRecorder.setOutputFile(mPath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = '" + filePath + "'", null, null); cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }
    */

    /*
    //데이터 로드 메소드
    public String loadAudioFile() {
        String sdPath;  //SD 카드의 경로
        String externalState = Environment.getExternalStorageState();
        if (externalState.equals(Environment.MEDIA_MOUNTED)) {
            //외부 저장 장치가 마운트 되어서 읽어올 준비가 되었을 때
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //마운트 되지 않았을 때
            sdPath = Environment.MEDIA_UNMOUNTED;
        }
        String result = "";
        try {
            String dir = sdPath + "/" + fileName + ".aac";
            //파일에서 읽어오기 위한 스트림 객체
            File file = new File(dir);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            result = new String(buffer);
            Log.d("RECORD_TEST",result.toString());
        } catch (Exception e) {
            Log.i("RECORD_TEST", "불러오기 실패: "+e.getMessage());
        }
        return result;
    }
    */



}
