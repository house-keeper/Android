package com.example.housekeeper_android.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.housekeeper_android.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WindowActivity extends AppCompatActivity {

    Toolbar window_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

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


    }

    //ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.door_menu, menu);
        return true;
    }

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
