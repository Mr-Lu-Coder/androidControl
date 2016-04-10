package com.example.test.app01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Activity_addCompute extends AppCompatActivity {

    private EditText name;
    private EditText mac;
    private EditText ip;

    private ImageView imagename;
    private ImageView imagemac;
    private ImageView imageip;

    private boolean nameok = false;
    private boolean macok = false;
    private boolean ipok = false;
    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcompute);
        ActivityCollector.addActivity(this);

        //获得从上个活动传来的token
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        name = (EditText)findViewById(R.id.compute_name);
        mac = (EditText)findViewById(R.id.compute_mac);
        ip = (EditText)findViewById(R.id.compute_ip);


        imagename = (ImageView)findViewById(R.id.imageView_computename);
        imagemac = (ImageView)findViewById(R.id.imageView_computemac);
        imageip = (ImageView)findViewById(R.id.imageView_computeip);
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
        bt_edit.setText("添加");
        tv_title.setText("添加机器");

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFormat()) {
                    sendHttpPost();
                }
            }
        });

        setTextListner();


    }

    public boolean sendHttpPost()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("computename", name.getText().toString()));
        params.add(new BasicNameValuePair("mac", mac.getText().toString()));
        params.add(new BasicNameValuePair("ip", ip.getText().toString()));
        String address = Hostname.hostname+"compute/"+token+"/";
        boolean res = HttpUtil.sendHttprequestPost(Activity_addCompute.this, address, params,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        //主线程进行操作
                        Activity_addCompute.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_addCompute.this, "添加成功下拉刷新", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Activity_addCompute.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(Activity_addCompute.this, "Post失败1", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNotFind() {
                        Activity_addCompute.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_addCompute.this, "Post失败2", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        return true;
    }
    public boolean checkFormat()
    {
        if (!nameok) {
            Toast.makeText(Activity_addCompute.this,"请输入名字",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!macok) {
            Toast.makeText(Activity_addCompute.this,"mac格式错误",Toast.LENGTH_SHORT).show();
            return false;
        }if (!ipok) {
        Toast.makeText(Activity_addCompute.this,"ip格式错误",Toast.LENGTH_SHORT).show();
        return false;
        }
        return true;
    }

    public void setTextListner()
    {
        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (name.getText().toString().length() > 0) {
                    imagename.setImageResource(R.mipmap.right);
                    nameok = true;
                } else {
                    nameok = false;
                    imagename.setImageResource(R.mipmap.wrong);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mac.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern = Pattern.compile("^[A-Fa-f0-9]{2}(-[A-Fa-f0-9]{2}){5}$");
                Matcher matcher = pattern.matcher(mac.getText().toString());
                if (matcher.matches()) {
                    imagemac.setImageResource(R.mipmap.right);
                    macok = true;
                } else {
                    imagemac.setImageResource(R.mipmap.wrong);
                    macok = false;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        ip.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
                Matcher matcher = pattern.matcher(ip.getText().toString());
                if (matcher.matches()) {
                    imageip.setImageResource(R.mipmap.right);
                    ipok = true;
                } else {
                    imageip.setImageResource(R.mipmap.wrong);
                    ipok = false;
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
