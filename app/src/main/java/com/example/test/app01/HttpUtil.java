package com.example.test.app01;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.net.URL;

/**
 * Created by lushangqi on 2016/3/9.
 */
import java.net.URL;
import java.util.List;
/**
 * 网络请求
 */
public class HttpUtil {
    /**
     * 发送Get 请求
     * @param mContext 上下文
     * @param address  访问的URL
     * @param listener 回调接口
     * @return
     */
    public static boolean sendHttpRequest(Context mContext,final String address, final
    HttpCallbackListener listener) {
        if (!isInternetAvailable(mContext))
        {
            Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("HttpUtilGet", "connect" + address);
                    HttpClient httpClient = new DefaultHttpClient();
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    int code = httpResponse.getStatusLine().getStatusCode();
                    Log.d("HttpUtil", "codeget"+code);
                    if (code/100 == 2) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        if (listener != null) {
                            listener.onFinish(response);
                        }
                    }
                    else {
                        Log.d("HttpUtil", "notfind");
                        listener.onNotFind(code);
                    }
                    Log.d("HttpUtil", "end");

                }catch (Exception e)
                {
                    Log.d("HttpUtil", "Exception");
                    listener.onError(e);
                }
            }
        }).start();
        return true;
    }

    /**
     * 发送 Post请求
     * @param mContext 上下文
     * @param address 访问的URL
     * @param params  请求参数
     * @param listener 回调接口
     * @return
     */
    public static boolean sendHttprequestPost(Context mContext, final String address,
                                              final List<NameValuePair> params,final HttpCallbackListener listener)
    {
        if (!isInternetAvailable(mContext))
        {
            Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("HttpUtilPost", "connect"+address);
                    HttpClient httpClient = new DefaultHttpClient();
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
                    HttpPost httpPost = new HttpPost(address);
                    UrlEncodedFormEntity entitypost = new UrlEncodedFormEntity(params, "utf-8");
                    httpPost.setEntity(entitypost);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    int code = httpResponse.getStatusLine().getStatusCode();
                    Log.d("HttpUtil", "codepost"+code);
                    if (code/100 == 2) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        if (listener != null) {
                            listener.onFinish(response);
                        }
                    } else {
                        Log.d("HttpUtilPost", "notfind");
                        listener.onNotFind(code);
                    }
                    Log.d("HttpUtilPost", "end");

                }catch (Exception e)
                {
                    Log.d("HttpUtilPost", "Exception");
                    listener.onError(e);
                }
            }
        }).start();
        return true;
    }

    /**
     * 检查是否有可用网络
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            return true;
        else return false;
    }


}
