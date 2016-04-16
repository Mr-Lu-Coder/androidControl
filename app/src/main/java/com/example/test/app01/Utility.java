package com.example.test.app01;



import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
/**
 * 处理服务器返回的数据
 */
public class Utility {

    /**
     * 将关于用户的数据以Person数组的形式返回
     * @param jsonData
     * @return ArrayList<Person>
     */
    public static ArrayList<Person> handlePersonresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Person> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Person>>(){}.getType());
        return tmpData;
    }

    /**
     * 将关于机器的数据以Computer数组的形式返回
     * @param jsonData
     * @return ArrayList<Computer>
     */
    public static ArrayList<Computer> handleComputeresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Computer> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Computer>>(){}.getType());
        return tmpData;
    }

    /**
     * 将关于Confirm的数据以Confirm数组的形式返回
     * @param jsonData
     * @return ArrayList<Confirm>
     */
    public static ArrayList<Confirm> handleConfirmresponse(String jsonData) {
        Gson gson = new Gson();
        ArrayList<Confirm> tmpData;
        tmpData = gson.fromJson(jsonData, new TypeToken<ArrayList<Confirm>>(){}.getType());
        return tmpData;
    }

    /**
     * 返回token字符串
     * @param res
     * @return String
     */
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
