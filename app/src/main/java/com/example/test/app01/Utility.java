package com.example.test.app01;

/**
 * Created by lushangqi on 2016/3/26.
 */

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    //解析处理服务器返回的数据
    public static ArrayList<Person> handlePersonresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Person> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Person>>(){}.getType());
        return tmpData;
    }
    public static ArrayList<Computer> handleComputeresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Computer> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Computer>>(){}.getType());
        return tmpData;
    }
    public static ArrayList<Confirm> handleConfirmresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Confirm> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Confirm>>(){}.getType());
        return tmpData;
    }

    public static String getStr_token(String res) {
        StringBuffer tmp = new StringBuffer();
        boolean flag = false;
        for (int i = 0; i < res.length(); i++) {
            if (flag && res.charAt(i) != ' ' && res.charAt(i) != '"') {
                tmp.append(res.charAt(i));
            }
            if (res.charAt(i) == ':') {
                flag = true;
            }
        }
        return tmp.toString();
    }

}
