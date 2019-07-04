package com.example.housekeeper_android.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.housekeeper_android.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WifiTestActivity extends AppCompatActivity {

    //UI Element
    Button btnUp;
    Button btnDown;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "192.168.0.76";
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
        protected Void doInBackground(Void... params){
            try{


//                socket = new java.net.Socket("jsmjsm.iptime.org",8885);
                InetAddress inetAddress = InetAddress.getByName(address);
                socket = new Socket(inetAddress,port);
                Log.d("소켓",socket.toString());

                OutputStream out = socket.getOutputStream();
                out.write(CMD.getBytes());

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
