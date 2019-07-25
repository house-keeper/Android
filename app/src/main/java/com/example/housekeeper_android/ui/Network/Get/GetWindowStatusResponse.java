package com.example.housekeeper_android.ui.Network.Get;

import javax.xml.transform.Result;

public class GetWindowStatusResponse {

    public String message;
    int status;
    Result result;

    public class Result{
        int w_status;
        public Integer getw_status() {return w_status;}

    }

    public Result getResult() {return result;}







}
