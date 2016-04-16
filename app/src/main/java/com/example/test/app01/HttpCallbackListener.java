package com.example.test.app01;

/**
 * Http网络请求时的回调接口
 * 通过实现接口中的方法，实现UI主线程对子线程中返回结果的处理
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
    void onNotFind(int rescode);

}
