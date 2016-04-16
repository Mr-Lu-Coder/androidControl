package com.example.test.app01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册用户
 */
public class Activity_addPerson extends AppCompatActivity {
    private EditText name;
    private EditText passwd;
    private EditText passwd2;
    private EditText email;
    private String TAG = "addPersonActivity";
    private String token;

    private ImageView imagename;
    private ImageView imagepasswd;
    private ImageView imagepasswd2;
    private ImageView imageemail;

    private int FLAG;
    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;

    private boolean nameok = false;
    private boolean passwdok = false;
    private boolean passwd2ok = false;
    private boolean emailok = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);

        FLAG = 0;
        name = (EditText)findViewById(R.id.register_name);
        passwd = (EditText)findViewById(R.id.register_passwd);
        passwd2 = (EditText)findViewById(R.id.register_passwd2);
        email = (EditText)findViewById(R.id.register_email);

        imagename = (ImageView)findViewById(R.id.imageView_username);
        imagepasswd = (ImageView)findViewById(R.id.imageView_passwd);
        imagepasswd2 = (ImageView)findViewById(R.id.imageView_passwd2);
        imageemail = (ImageView)findViewById(R.id.imageView_email);
        //为文本设置监听
        setTextListener();
        //获得title
        bt_back = (Button)findViewById(R.id.button_back);
        bt_edit = (Button)findViewById(R.id.button_edit);
        tv_title = (TextView)findViewById(R.id.tv_title);
        bt_back.setText("返回");
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_edit.setText("注册");
        tv_title.setText("注册账号");

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write here
                //格式正确开始发送请求
                if (checkformat()) {
                    getDefaulttoken();
                }
            }
        });

    }

    /**
     * 通过HttpGet方式获得默认的Token
     * 注册用户时Url请求有token验证
     * @return
     */
    public boolean getDefaulttoken() {
        boolean res = HttpUtil.sendHttpRequest(Activity_addPerson.this, Hostname.hostname+
                        "username/" + Hostname.defaultname + "/" + Hostname.defaultpasswd + "/",
                new HttpCallbackListener() {


                    @Override
                    public void onFinish(String response) {
                        Log.d(TAG, response);
                        token = Utility.getStr_token(response);
                        //主线程进行操作
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //获取token后开始调用httppost
                                sendPostRequest();
                            }
                        });

                    }
                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        //主线程进行操作
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_addPerson.this, "获取token失败1", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    @Override
                    public void onNotFind(final int rescode) {
                        //主线程进行操作
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(rescode/100 == 4)
                                    Toast.makeText(Activity_addPerson.this, "连接超时", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Activity_addPerson.this, "服务器内部错误", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                }
        );
        return true;
    }

    /**
     * 发送HttpPost注册用户
     * @return
     */
    public boolean sendPostRequest() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name.getText().toString()));
        params.add(new BasicNameValuePair("email", email.getText().toString()));
        MD5 md5 = new MD5();
        Log.d(TAG, md5.GetMD5Code(passwd.getText().toString()));
        params.add(new BasicNameValuePair("passwd", md5.GetMD5Code(passwd.getText().toString())));
        params.add(new BasicNameValuePair("role", "普通用户"));
        String address = Hostname.hostname+"user/"+token+"/";
        boolean res = HttpUtil.sendHttprequestPost(Activity_addPerson.this, address, params,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        //主线程进行操作
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_addPerson.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(Activity_addPerson.this, "Post失败1", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNotFind(final int rescode) {
                        Activity_addPerson.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(rescode/100 == 4)
                                    Toast.makeText(Activity_addPerson.this, "注册失败", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Activity_addPerson.this, "服务器内部错误", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
        return true;
    }

    /**
     * 检查用户输入的格式
     * @return
     */
    public boolean checkformat() {
        if (!nameok) {
            Toast.makeText(Activity_addPerson.this, "请输入名字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwdok) {
            Toast.makeText(Activity_addPerson.this, "密码长度不小于6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwd2ok) {
            if(passwd2.getText().toString().length() < 6)
                Toast.makeText(Activity_addPerson.this, "密码长度不小于6位", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Activity_addPerson.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!emailok) {
            Toast.makeText(Activity_addPerson.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 对EditText设置监听，检查格式
     */
    public void setTextListener()
    {

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (name.getText().toString().length() > 0) {
                    imagename.setImageResource(R.mipmap.right);
                    nameok = true;
                } else {
                    imagename.setImageResource(R.mipmap.wrong);
                    nameok = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        passwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (passwd.getText().toString().length() >= 6 ) {
                    imagepasswd.setImageResource(R.mipmap.right);
                    passwdok = true;
                } else {
                    imagepasswd.setImageResource(R.mipmap.wrong);
                    passwdok = false;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        passwd2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (passwd2.getText().toString().length() >= 6 &&
                        passwd.getText().toString().equals(passwd2.getText().toString())) {
                    imagepasswd2.setImageResource(R.mipmap.right);
                    passwd2ok = true;
                } else {
                    imagepasswd2.setImageResource(R.mipmap.wrong);
                    passwd2ok = false;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@.+\\.+[a-z]+");
                Matcher matcher = pattern.matcher(email.getText().toString());
                Log.d("addperson", email.getText().toString());
                if (matcher.matches()) {
                    imageemail.setImageResource(R.mipmap.right);
                    emailok = true;
                } else {
                    imageemail.setImageResource(R.mipmap.wrong);
                    emailok = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
