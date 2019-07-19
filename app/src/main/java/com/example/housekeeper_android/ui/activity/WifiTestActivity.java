package com.example.housekeeper_android.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.housekeeper_android.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import retrofit2.http.Url;

public class WifiTestActivity extends AppCompatActivity {


    private URL Url;
    private String strUrl,strCookie,result;

    //UI Element
    Button btnUp;
    Button btnDown;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "http://192.168.0.76";
    public static int wifiModulePort = 8885;
    public static String CMD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_test);

        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);

        txtAddress = (EditText) findViewById(R.id.ipAddress);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getIPandPort();
                CMD = "Up";

                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask(wifiModuleIp,wifiModulePort);
                cmd_increase_servo.execute();
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getIPandPort();
                CMD = "Down";

                Socket_AsyncTask cmd_decrease_servo = new Socket_AsyncTask(wifiModuleIp,wifiModulePort);
                cmd_decrease_servo.execute();
            }
        });

    }

    public void getIPandPort()
    {
//        String iPandPort = txtAddress.getText().toString();
//        Log.d("MYTEST","IP String: "+ iPandPort);
//        String temp[]= iPandPort.split(":");
//        wifiModuleIp = temp[0];
//        wifiModulePort = Integer.valueOf(temp[1]);
    }
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {

        String address;
        int port;
        String response;

        Socket_AsyncTask(String wifiModuleIp,int wifiModulePort) {
            this.address = wifiModuleIp;
            this.port = wifiModulePort;
            Log.d("TEST","생성");
        }

        Socket socket = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://192.168.0.76:8885"; //탐색하고 싶은 URL이다.
        }

        @Override
        protected Void doInBackground(Void... params){
            try{

                Url = new URL(strUrl); // URL화 한다.
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                conn.setRequestMethod("GET"); // get방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

//                strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

                InputStream is = conn.getInputStream(); //input스트림 개방

                StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8")); //문자열 셋 세팅
                String line;

                line = CMD;

                while ((line = reader.readLine()) != null) {
                    builder.append(line+ "\n");
                }

                result = builder.toString();



//                socket = new java.net.Socket("jsmjsm.iptime.org",8885);
                InetAddress inetAddress = InetAddress.getByName(address);
                socket = new Socket(inetAddress,port);
                Log.d("소켓",socket.toString());

                OutputStream out = socket.getOutputStream();
//                out.write(CMD.getBytes());

                out.write(result.getBytes());
//                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                dataOutputStream.writeBytes(CMD);
//                dataOutputStream.close();

                Log.d("TEST","IP:" +wifiModuleIp);
                Log.d("TEST","PORT:"+wifiModulePort);
                Log.d("TEST",CMD);

            }catch (UnknownHostException e){
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();


            }catch (IOException e){
                e.printStackTrace();
                response = "IOException: " + e.toString();


            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
