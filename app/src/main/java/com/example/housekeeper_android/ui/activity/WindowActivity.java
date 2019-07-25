package com.example.housekeeper_android.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.housekeeper_android.R;
import com.example.housekeeper_android.ui.Network.ApplicationController;
import com.example.housekeeper_android.ui.Network.Get.GetWindowStatusResponse;
import com.example.housekeeper_android.ui.Network.NetworkService;
import com.example.housekeeper_android.ui.Network.Post.PostRecordFileResponse;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusRequest;
import com.example.housekeeper_android.ui.Network.Post.PostWindowStatusResponse;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WindowActivity extends AppCompatActivity {

    Toolbar window_toolbar;
    Switch window_status_switch;
    TextView window_status_text;
    public static String wifiModuleIp = "192.168.0.28";
    public static int wifiModulePort = 8080;
    public static String CMD = "0";
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        networkService = ApplicationController.getInstance().getNetworkService();

        //툴바 관련
        window_toolbar = (Toolbar) findViewById(R.id.window_toolbar);
        setSupportActionBar(window_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.bar_button_return);

        //
        TextView textView = (TextView)findViewById(R.id.weatherTv);
        WeatherConnection weatherConnection = new WeatherConnection();
        AsyncTask<String, String, String> result = weatherConnection.execute("","");
        System.out.println("RESULT");

        try{
            String msg = result.get();
            System.out.println(msg);

            textView.setText(msg.toString());

        }catch (Exception e){

        }

       window_status_text=(TextView)findViewById(R.id.window_status);
        //창문 열/닫 값 get
        Call<GetWindowStatusResponse> GetWindowStatusResponseCall = networkService.getWindowStatus();
        GetWindowStatusResponseCall.enqueue(new Callback<GetWindowStatusResponse>() {
            @Override
            public void onResponse(Call<GetWindowStatusResponse> call, Response<GetWindowStatusResponse> response) {
                Log.d("WINDOW_RESPONSE_TEST",String.valueOf(response.body()));
//                Toast.makeText(getApplicationContext(),String.valueOf(response.body().responseMessage),Toast.LENGTH_SHORT);

                if(Integer.valueOf(response.body().result) == 1){
                    window_status_text.setText("열림");
                    window_status_switch.setChecked(true);
                    Log.d("창문값절달: ","window open");
                }else if(response.body().result== 0){
                    window_status_text.setText("닫힘");
                    window_status_switch.setChecked(false);
                }

            }

            @Override
            public void onFailure(Call<GetWindowStatusResponse> call, Throwable t) {

            }
        });

        Log.d("호출테스트: ","onCreate");

        //창문 툴바
        window_status_switch = (Switch) findViewById(R.id.window_status_btn);
        window_status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    CMD = "1";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

/**
                    // TODO: 창문 값 바꿔줘야함
                    Call<PostWindowStatusResponse> PostWindowStatusResponseCall = networkService.postWindowStatus(new PostWindowStatusRequest(1));
                    PostWindowStatusResponseCall.enqueue(new Callback<PostWindowStatusResponse>() {
                        @Override
                        public void onResponse(Call<PostWindowStatusResponse> call, Response<PostWindowStatusResponse> response) {
                            Log.d("WINDOW_RESPONSE_TEST",String.valueOf(response.body()));
                            Toast.makeText(getApplicationContext(),String.valueOf(response.body().responseMessage),Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Call<PostWindowStatusResponse> call, Throwable t) {

                        }
                    });
 **/

                } else if(isChecked){
                    // The toggle is disabled
                    CMD = "0";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();
                }
            }
        });

    }

    //ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.door_menu, menu);
        return true;
    }

    //소켓
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(WindowActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,WindowActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
//                dataOutputStream.writeUTF(CMD);
                dataOutputStream.flush();
                dataOutputStream.close();


                Log.d("DATA:: ",CMD.toString());
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }

    /////날씨 크롤링
    // 네트워크 작업은 AsyncTask 를 사용
    public class WeatherConnection extends AsyncTask<String, String, String>{

        // 백그라운드에서 작업
        @Override
        protected String doInBackground(String... params) {

            // Jsoup을 이용한 날씨데이터 Pasing
            try{
                String path = "https://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09320105";

                Document document = Jsoup.connect(path).get();

                Elements elements1 = document.select("div.fl h5");
                Elements elements2 = document.select("div.fl em");
                Elements elements3 = document.select("div.fl p strong");
                Elements elements4 = document.select("div.fl p a span");

                System.out.println(elements1);
                System.out.println(elements2);
                System.out.println(elements3);
                System.out.println(elements4);

                Element targetElement1 = elements1.get(0);
                Element targetElement2 = elements2.get(0);
                Element targetElement3 = elements3.get(1);
                Element targetElement4 = elements4.get(0);

                String text1 = targetElement1.text();
                String text2 = targetElement2.text();
                String text3 = "강수확률 " + targetElement3.text() + "%";
                String text4 = targetElement4.text();


                String text = text1 + '\n' + text2 + '\n' + text3 + '\n' + text4;


                return text;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}