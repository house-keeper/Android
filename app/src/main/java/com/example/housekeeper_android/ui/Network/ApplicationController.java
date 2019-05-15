package com.example.housekeeper_android.ui.Network;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    public final static String baseUrl = "http://13.124.163.108/";
    private NetworkService networkService;
    private static ApplicationController instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        buildNetwork();
    }

    public void buildNetwork(){
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);
    }

    public static ApplicationController getInstance() {
        return instance;
    }
    public NetworkService getNetworkService() {
        return networkService;
    }
}
