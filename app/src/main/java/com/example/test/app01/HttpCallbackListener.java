package com.example.test.app01;

/**
 * Created by lushangqi on 2016/3/9.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
    void onNotFind();

}
