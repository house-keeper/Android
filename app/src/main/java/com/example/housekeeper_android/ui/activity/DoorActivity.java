package com.example.housekeeper_android.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Adapter.RecordRVAdapter;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetRecordListResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.audiofx.AudioEffect.ERROR;


public class DoorActivity extends AppCompatActivity {


    Toolbar door_toolbar;
    TextView interphone_outsider_message;
    EditText interphone_my_message;
    LinearLayout interphone_my_message_view;
    Button interphone_capture_btn, interphone_send_message_btn, interphone_send_record_btn;
    private TextToSpeech tts;

    private WebView door_streaming;
    ServerSocket serverSocket;
    public static String outsider_message="";
    public static String rpi_confirm_message="";

    public static String wifiModuleIp = "192.168.0.28";
    public static int wifiModulePort = 8080;
    public static String CMD = "0";
    public static String real_text = "";

    Thread socketServerThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        Log.d("생명주기: ","onCreate");

        Context ctx = this;


        door_toolbar = (Toolbar) findViewById(R.id.door_toolbar);
        door_streaming=(WebView)findViewById(R.id.door_webview);
        interphone_capture_btn=(Button)findViewById(R.id.interphone_capture_btn);
        interphone_outsider_message=(TextView)findViewById(R.id.interphone_outsider_message);
        interphone_my_message=(EditText)findViewById(R.id.interphone_my_message);
        interphone_my_message_view=(LinearLayout)findViewById(R.id.interphone_my_message_view);
        interphone_send_message_btn=(Button)findViewById(R.id.interphone_send_message_btn);
        interphone_send_record_btn=(Button)findViewById(R.id.interphone_send_record_btn);


        //툴바 관련
        setSupportActionBar(door_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.bar_button_return);

        //웹뷰 관련
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

        //dooractivity 들어오면 라즈베리에게 신호 전송
        CMD = "1";
        DoorActivity.Socket_AsyncTask rpi_connection_confirm = new DoorActivity.Socket_AsyncTask();
        rpi_connection_confirm.execute();

        //인터폰 외부인 텍스트 받기
        socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();


        //인터폰 사용자 입력 관련 // 사용자가 텍스트 창 누르면 텍스트 자동 초기화
        interphone_my_message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interphone_my_message.setText("");
                interphone_my_message.setFocusableInTouchMode(true);
                interphone_my_message.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        //인터폰 사용자 메세지 전송 관련
        interphone_send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD ="tts";
                real_text=interphone_my_message.getText().toString();
                DoorActivity.Socket_AsyncTask tts_send_text = new DoorActivity.Socket_AsyncTask();
               tts_send_text.execute();
            }
        });

        final ArrayList<Integer> recordIdxList = new ArrayList<>();

        //인터폰 음성녹음 전송 관련
        interphone_send_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        DoorActivity.this);
                alertBuilder.setIcon(R.drawable.mic);
                alertBuilder.setTitle("전송할 녹음을 하나만 선택하세요.");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        DoorActivity.this,
                        android.R.layout.simple_list_item_1);

                Call<GetRecordListResponse> getRecordListResponseCall = ApplicationController.getInstance().getNetworkService().getRecordList();
                getRecordListResponseCall.enqueue(new Callback<GetRecordListResponse>() {
                    @Override
                    public void onResponse(Call<GetRecordListResponse> call, Response<GetRecordListResponse> response) {
                        Log.d("RESPONSE_TEST",response.body().result.toString());
                        for(int i=0; i<response.body().result.size(); i++){
                            adapter.add(response.body().result.get(i).fileName);
                            recordIdxList.add(response.body().result.get(i).idx);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<GetRecordListResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"FAIL",Toast.LENGTH_SHORT);
                    }
                });

                // 버튼 생성
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });


                alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer recordIdx = recordIdxList.get(which);
                        // TODO: 녹음 라즈베리파이에 보내기
//                        Toast.makeText(DoorActivity.this, "성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(DoorActivity.this, "전송이 실패하였습니다.", Toast.LENGTH_SHORT).show();


                    }
                });


                alertBuilder.show();

            }
        });
    }

    ///////////////////////////////////////////////////////////////
    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;
        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);

                while (true) {
                    System.out.println("클라이언트 접속 대기 중...");
                  //  serverSocket.bind(SocketServerPORT);
                    Socket socket = serverSocket.accept();
                    Log.d("TESTTEST",socket.getInetAddress().toString());
                    System.out.println(socket.getInetAddress() + "가 접속되었습니다.");

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // 클라이언트로부터 메시지 입력받음
                    outsider_message= bufferedReader.readLine();

                    JsonParser parser = new JsonParser();
                    final JsonElement element = parser.parse(outsider_message);

                    DoorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          interphone_outsider_message.setText(element.getAsJsonObject().get("text").getAsString());
                        }
                    });
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    //////////////////////////////////////////////////////


    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(DoorActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,DoorActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(URLEncoder.encode(CMD, "utf-8"));
              //  dataOutputStream.writeChars(CMD);
            //    dataOutputStream.writeUTF(CMD);
         //       Log.d("output",dataOutputStream.toString());
                dataOutputStream.flush();
               // dataOutputStream.close();
                Log.d("DATA:: ",CMD.toString());

                //tts test
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d("data","here");
                rpi_confirm_message=br.readLine();
                Log.d("DATA::fromrpi",rpi_confirm_message);
               // dis2.close();

                //tts connection success. send text
                if (rpi_confirm_message.equals("send text")){
                    Log.d("DATA::fromrpi","writing");

                //    BufferedWriter bw = new BufferedWriter(
                //            new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
               //     bw.write(real_text+"\r\n");

                    dataOutputStream.writeBytes(URLEncoder.encode(real_text, "UTF-8"));
                  //  dataOutputStream.writeBytes(real_text);
                    Log.d("DATA::output",real_text);
                    dataOutputStream.flush();
                    Log.d("DATA::output2","flushed");
                   // dataOutputStream.close();
                }else if(rpi_confirm_message.equals("send address")){
                    Log.d("DATA::fromrpi","address writing");

                }
                else dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
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

    @Override
    protected void onPause() {
        super.onPause();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        CMD = "0";
        DoorActivity.Socket_AsyncTask cmd_increase_servo = new DoorActivity.Socket_AsyncTask();
        cmd_increase_servo.execute();
    }


}

