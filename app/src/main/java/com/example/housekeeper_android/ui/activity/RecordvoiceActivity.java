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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.RecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetRecordListResponse;
import com.example.housekeeper_android.ui.Network.NetworkService;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;

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
    EditText etFilename;
    MediaRecorder mRecorder=null;
    MediaPlayer mPlayer=null;
    String fileName=""; //
    String mPath="";
    String todayDate="";
    boolean isRecording = false;
    boolean isPlaying = false;


    RecyclerView rvRecord;
    RecordRVAdapter recordRVAdapter;


    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvoice);

        networkService = ApplicationController.getInstance().getNetworkService();

        btnRecord = (Button)findViewById(R.id.btnRecord);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnSave = (Button)findViewById(R.id.btnSave);
        etFilename = (EditText)findViewById(R.id.etFileName);
        mRecorder = new MediaRecorder();

        initRecordList(); // 녹음리스트 불러오기
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(mPath);
                RequestBody fileName = RequestBody.create(MediaType.parse("text/pain"), etFilename.getText().toString());

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
               MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file","test.mp4", requestFile);

                Call<PostRecordFileResponse> postRecordFileResponseCall = networkService.postRecordFile(uploadFile,fileName);
                postRecordFileResponseCall.enqueue(new Callback<PostRecordFileResponse>() {
                    @Override
                    public void onResponse(Call<PostRecordFileResponse> call, Response<PostRecordFileResponse> response) {
                        Log.d("RESPONSE_TEST",String.valueOf(response.body()));
                        if (response.isSuccessful()){
                            Toast.makeText(RecordvoiceActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT);
                            initRecordList();
//                            recordRVAdapter.notifyItemChanged(recordRVAdapter.getItemCount()+1);
//                            recordRVAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostRecordFileResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"FAIL",Toast.LENGTH_SHORT);
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
                    mRecorder.release();

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
        fileName = etFilename.getText().toString();


       mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+fileName+".mp4";
      //  mPath = Context.getFilesDir().getAbsolutePath
        Log.d("RECORD_TEST", "file path is " + mPath);
        mRecorder.setOutputFile(mPath);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initRecordList(){

        rvRecord = (RecyclerView)findViewById(R.id.rvRecordList);

        Call<GetRecordListResponse> getRecordListResponseCall = networkService.getRecordList();
        getRecordListResponseCall.enqueue(new Callback<GetRecordListResponse>() {
            @Override
            public void onResponse(Call<GetRecordListResponse> call, Response<GetRecordListResponse> response) {
                Log.d("RESPONSE_TEST",String.valueOf(response.body()));
                recordRVAdapter = new RecordRVAdapter(getBaseContext(),response.body().result);
                rvRecord.setAdapter(recordRVAdapter);
                rvRecord.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                rvRecord.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<GetRecordListResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"FAIL",Toast.LENGTH_SHORT);
            }
        });
    }

}
