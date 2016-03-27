package com.example.test.app01;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.StringBuffer;
/**
 * Created by lushangqi on 2016/3/9.
 */
public class Activity_login extends AppCompatActivity  implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText et_user;
    private EditText et_passwd;
    private Button bt_login;
    private Button bt_forget;
    private CheckBox rememberPass;
    private String str_token = new String("test");
    private String TAG = new String("LoginActivity");
    private boolean isconnect = false;

    ProgressDialog progressDialog;



    public static final int CONNECT_OK = 0;
    public static final int CONNECT_FAIL = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "creat");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        et_user = (EditText)findViewById(R.id.et_username);
        et_passwd = (EditText)findViewById(R.id.et_password);
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_forget = (Button)findViewById(R.id.bt_forgetPwd);
        rememberPass = (CheckBox)findViewById(R.id.cbRememberPwd);
        boolean isRemember = pref.getBoolean("remember", false);
        if (isRemember) {
            String user = pref.getString("user", "");
            String passwd = pref.getString("passwd", "");
            Log.d(TAG, "***"+user+" "+passwd);
            et_user.setText(user);
            et_passwd.setText(passwd);
            rememberPass.setChecked(true);
        }
        else {
            String user = pref.getString("user", "");
            if (user.length() > 0) {
                et_user.setText(user);
            }
        }



        bt_login.setOnClickListener(this);
        bt_forget.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
            {
                Log.d(TAG, "login");
                String user = et_user.getText().toString();
                String passwd = et_passwd.getText().toString();
                if (user.length() == 0) {
                    Toast.makeText(Activity_login.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (passwd.length() == 0) {
                    Toast.makeText(Activity_login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!HttpUtil.isInternetAvailable(this)) {
                    Toast.makeText(Activity_login.this, "没有可用网络", Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.d(TAG, "this");
                isconnect = false;
                progressDialog = new ProgressDialog(Activity_login.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //此处需要注意执行远程登录函数时，有可能已经主线程的函数已经执行完了
                LoginConnect(user, passwd);

                try {
                    Thread.sleep(1000); //暂停，每一秒输出一次
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "bforechange");
                if (isconnect) {
                    progressDialog.dismiss();
                    editor = pref.edit();
                    if (rememberPass.isChecked()) {
                        Log.d(TAG, "rempasswd");
                        editor.putBoolean("remember", true);
                        editor.putString("user", user);
                        editor.putString("passwd", passwd);
                    } else {
                        editor.clear();
                        editor.putString("user", user);
                    }
                    editor.commit();
                    //write here
                    Log.d(TAG, "toMainactivity");
                    Intent intent = new Intent(Activity_login.this, Activity_user.class);
                    intent.putExtra("token", str_token);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Activity_login.this, "连接超时或账号密码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.bt_forgetPwd:
            {
                Intent intent = new Intent(Activity_login.this, Activity_forget.class);
                startActivity(intent);
                Log.d(TAG, "forget");
                break;
            }
            default:
                break;
        }
    }

    public String getStr_token(String res) {
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

    public boolean LoginConnect(String user, String passwd) {
        //write here

        Log.d(TAG, user + "tryconnect");
        boolean res = HttpUtil.sendHttpRequest(Activity_login.this, Hostname.hostname+"username/" + user + "/" + passwd + "/",
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Log.d(TAG, response);
                        str_token = getStr_token(response);
                        isconnect = true;
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        isconnect = false;
                    }

                    @Override
                    public void onNotFind() {
                        isconnect = false;
                    }


                }
        );
        Log.d(TAG, "inconnect");
        if (res) return true;
        else return false;

    }

}
