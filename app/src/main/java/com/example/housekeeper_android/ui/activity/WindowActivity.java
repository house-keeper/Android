package com.example.housekeeper_android.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.housekeeper_android.ui.etc.SharedPrefrernceController;

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
    TextView window_status_text, weather_status_text, textView;
    ImageView weather_img, next_img, next_img2;
    LinearLayout main_ui;

    public static String wifiModuleIp = "192.168.0.28";
    public static int wifiModulePort = 8080;
    public static String CMD = "0";
    NetworkService networkService;
    boolean isFirstCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        networkService = ApplicationController.getInstance().getNetworkService();
        weather_img = (ImageView)findViewById(R.id.weather_img);
        next_img = (ImageView)findViewById(R.id.next_img);
        next_img2 = (ImageView)findViewById(R.id.next_img2);
        weather_status_text =(TextView)findViewById(R.id.weather_status_text);
        main_ui =(LinearLayout)findViewById(R.id.main_ui);
        isFirstCreate = true;


        //
        textView = (TextView)findViewById(R.id.weatherTv);
        mainWeatherConnection weatherConnection = new mainWeatherConnection();
        AsyncTask<String, String, String> result = weatherConnection.execute("","");
        System.out.println("RESULT");

        try{
            String msg = result.get();
            System.out.println(msg);
            textView.setText(msg.toString());
        }catch (Exception e){

        }

        TextView next_weather_text =(TextView)findViewById(R.id.next_weather);
        nextWeatherConnection nextweatherConnection = new nextWeatherConnection();
        AsyncTask<String, String, String> result2 = nextweatherConnection.execute("","");
        System.out.println("RESULT2");
        try{
            String msg2 = result2.get();
            System.out.println(msg2);
            next_weather_text.setText(msg2.toString());
        }catch (Exception e){

        }

        TextView next_weather_text2 =(TextView)findViewById(R.id.next_weather2);
        nextWeatherConnection2 nextweatherConnection2 = new nextWeatherConnection2();
        AsyncTask<String, String, String> result3 = nextweatherConnection2.execute("","");
        System.out.println("RESULT3");
        try{
            String msg3 = result3.get();
            System.out.println(msg3);
            next_weather_text2.setText(msg3.toString());
        }catch (Exception e){

        }


       window_status_text=(TextView)findViewById(R.id.window_status);
        //창문 열/닫 값 get
        Call<GetWindowStatusResponse> GetWindowStatusResponseCall = networkService.getWindowStatus();
        GetWindowStatusResponseCall.enqueue(new Callback<GetWindowStatusResponse>() {
            @Override
            public void onResponse(Call<GetWindowStatusResponse> call, Response<GetWindowStatusResponse> response) {
                Log.d("WINDOW_RESPONSE_TEST",String.valueOf(response.body().getResult().getw_status()));
                Toast.makeText(getApplicationContext(),String.valueOf(response.body().message),Toast.LENGTH_SHORT);

                if(response.body().getResult().getw_status() == 1){
                    window_status_text.setText("열림");
                    window_status_switch.setChecked(true);
                    Log.d("창문초기값1 ","window opened");
                }else if(response.body().getResult().getw_status()== 0){
                    window_status_text.setText("닫힘");
                    window_status_switch.setChecked(false);
                    Log.d("창문초기값2 ","window closed");
                }
            }
            @Override
            public void onFailure(Call<GetWindowStatusResponse> call, Throwable t) {
            }
        });
        Log.d("호출테스트: ","onCreate");

        window_status_switch = (Switch) findViewById(R.id.window_status_btn);

        window_status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("SWITCHTEST",String.valueOf(isFirstCreate));
                if(!isFirstCreate){
                    if (isChecked) {
                        // The toggle is enabled
                        CMD = "1";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        window_status_text.setText("열림");
                    } else if(!isChecked){
                        // The toggle is disabled
                        CMD = "0";
                        window_status_text.setText("닫힘");
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                    }
                }
                isFirstCreate = false;


            }
        });

        //툴바 관련
        window_toolbar = (Toolbar) findViewById(R.id.window_toolbar);
        setSupportActionBar(window_toolbar);
        getSupportActionBar().setTitle("");
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstCreate = false;
    }

    ////////////////////////////////////////


    ///////////////////////////////////////////


    //창문 열/닫 위한 소켓통신
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

    //ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.door_menu, menu);
        return true;
    }

    /////날씨 크롤링
    // 네트워크 작업은 AsyncTask 를 사용
    public class mainWeatherConnection extends AsyncTask<String, String, String>{

        // 백그라운드에서 작업
        @Override
        protected String doInBackground(String... params) {

            // Jsoup을 이용한 날씨데이터 Pasing
            try{
                String path = "https://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09320105";

                Document document = Jsoup.connect(path).get();

                Elements elements1 = document.select("div.fl h5");
                Elements elements2 = document.select("div.fl em");
                Elements elements5 = document.select("div.fl em strong");
                Elements elements3 = document.select("div.fl p strong");
                Elements elements4 = document.select("div.fl p a span");


                System.out.println(elements1);
                System.out.println(elements2);
                System.out.println(elements3);
                System.out.println(elements4);
                System.out.println(elements5);


                Element targetElement1 = elements1.get(0);
                Element targetElement2 = elements2.get(0);
                Element targetElement3 = elements3.get(1);
                Element targetElement4 = elements4.get(0);
                Element targetElement5 = elements5.get(0);

                String text1 = targetElement1.text();
                String text2 = targetElement2.text();
                String text3 = "강수확률 " + targetElement3.text() + "%";
                String text4 = targetElement4.text();
                String text5 = targetElement5.text();

                if (text5.equals("맑음")){
                    weather_img.setImageResource(R.drawable.weather_status_sun);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.sunny_bg);
                }else if (text5.equals("흐림")){
                    weather_img.setImageResource(R.drawable.weather_status_cloudy);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.cloudy_bg);
                }else if (text5.equals("구름많음") || text5.equals("구름조금") ){
                    weather_img.setImageResource(R.drawable.weather_status_suncloud);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.suncloud_bg);
                }else if (text5.equals("비") || text5.equals("흐리고 비") || text5.equals("소나기") || text5.equals("구름많고 한때 소나기")){
                    weather_img.setImageResource(R.drawable.weather_status_rain);
                    main_ui.setBackgroundResource(R.drawable.rain_bg);
                    weather_status_text.setText(text5);
                }else if (text5.equals("눈") || text5.equals("진눈깨비")){
                    weather_img.setImageResource(R.drawable.weather_status_snow);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.snow_bg);
                }else if (text5.equals("뇌우")){
                    weather_img.setImageResource(R.drawable.weather_status_storm);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.storm_bg);
                }else if (text5.equals("흐려짐")){
                    weather_img.setImageResource(R.drawable.weather_status_cloud);
                    weather_status_text.setText(text5);
                    main_ui.setBackgroundResource(R.drawable.cloud_bg);
                }


                String text = text1 + '\n' + text2 + '\n' + text3 + '\n' + text4;
                return text;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class nextWeatherConnection extends AsyncTask<String, String, String>{

        // 백그라운드에서 작업
        @Override
        protected String doInBackground(String... params) {

            // Jsoup을 이용한 날씨데이터 Pasing
            try{
                String path = "https://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09320105";

                Document document = Jsoup.connect(path).get();

                Elements elements1 = document.select("li.after_h h6");
                Elements elements2 = document.select("div.inner p");

                System.out.println(elements1);
                System.out.println(elements2);

                Element targetElement1 = elements1.get(0);
                Element targetElement2 = elements2.get(0);

                String text1 = targetElement1.text();
                String text2 = targetElement2.text();


                if (text2.equals("맑음")){
                    next_img.setImageResource(R.drawable.weather_status_sun2);
                }else if (text2.equals("흐림")){
                    next_img.setImageResource(R.drawable.weather_status_cloudy2);
                }else if (text2.equals("구름많음") || text2.equals("구름조금") ){
                    next_img.setImageResource(R.drawable.weather_status_suncloud2);
                }else if (text2.equals("비") || text2.equals("흐리고 비") || text2.equals("소나기") || text2.equals("구름많고 한때 소나기")){
                    next_img.setImageResource(R.drawable.weather_status_rain2);
                }else if (text2.equals("눈") || text2.equals("진눈깨비")){
                    next_img.setImageResource(R.drawable.weather_status_snow2);
                }else if (text2.equals("뇌우")){
                    next_img.setImageResource(R.drawable.weather_status_storm2);
                }else if (text2.equals("흐려짐")){
                    next_img.setImageResource(R.drawable.weather_status_cloud2);
                }


                String text = text1 + '\n' + text2;
                return text;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class nextWeatherConnection2 extends AsyncTask<String, String, String>{

        // 백그라운드에서 작업
        @Override
        protected String doInBackground(String... params) {

            // Jsoup을 이용한 날씨데이터 Pasing
            try{
                String path = "https://weather.naver.com/rgn/townWetr.nhn?naverRgnCd=09320105";

                Document document = Jsoup.connect(path).get();

                Elements elements1 = document.select("li.after_h h6");
                Elements elements2 = document.select("div.inner p");

                System.out.println(elements1);
                System.out.println(elements2);

                Element targetElement1 = elements1.get(1);
                Element targetElement2 = elements2.get(1);

                String text1 = targetElement1.text();
                String text2 = targetElement2.text();


                if (text2.equals("맑음")){
                    next_img2.setImageResource(R.drawable.weather_status_sun2);
                }else if (text2.equals("흐림")){
                    next_img2.setImageResource(R.drawable.weather_status_cloudy2);
                }else if (text2.equals("구름많음") || text2.equals("구름조금") ){
                    next_img2.setImageResource(R.drawable.weather_status_suncloud2);
                }else if (text2.equals("비") || text2.equals("흐리고 비") || text2.equals("소나기") || text2.equals("구름많고 한때 소나기")){
                    next_img2.setImageResource(R.drawable.weather_status_rain2);
                }else if (text2.equals("눈") || text2.equals("진눈깨비")){
                    next_img2.setImageResource(R.drawable.weather_status_snow2);
                }else if (text2.equals("뇌우")){
                    next_img2.setImageResource(R.drawable.weather_status_storm2);
                }else if (text2.equals("흐려짐")){
                    next_img2.setImageResource(R.drawable.weather_status_cloud2);
                }


                String text = text1 + '\n' + text2;
                return text;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.door_menu_alarm:
                if (SharedPrefrernceController.getAddress(this).length() == 0){
                    Toast.makeText(this, "아직 주소를 저장하지 않았습니다. 설정에서 저장해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Uri smsUri = Uri.parse("sms:"+"112");
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
                    sendIntent.putExtra("sms_body", SharedPrefrernceController.getAddress(this)+" 여기에 지금 도둑이 들어왔어요 도와주세요");
                    startActivity(sendIntent);
                }
                break;
            case R.id.door_menu_record:
                startActivity(new Intent(WindowActivity.this,RecordActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}