package com.example.test.app01;

import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URL;

/**
 * Created by lushangqi on 2016/3/9.
 */
import java.net.URL;

public class HttpUtil {
    public static void sendHttpRequest(final String address, final
    HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("HttpUtil", "connect");
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        if (listener != null) {
                            listener.onFinish(response);
                        }
                    }
                    else {
                        Log.d("HttpUtil", "notfind");
                        listener.onNotFind();
                    }
                    Log.d("HttpUtil", "end");

                }catch (Exception e)
                {
                    Log.d("HttpUtil", "Exception");
                    listener.onError(e);
                }
            }
        }).start();
    }
}
