package com.example.test.app01;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 机器详细
 */
public class Activity_computeDetail extends AppCompatActivity {
    private TextView tv_id;
    private TextView tv_state;
    private TextView tv_mac;
    private TextView tv_ip;
    private Button button;
    private String token;

    private final int TO_SHUTDOWN = 0;
    private final int TO_SETUP = 1;
    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;

    private Computer curcompute;
    private String address = " ";
    private String tostatestr = " ";
    private String reversestate = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computedetail);
        ActivityCollector.addActivity(this);

        tv_id = (TextView)findViewById(R.id.textView_id);
        tv_state = (TextView)findViewById(R.id.textView_state);
        tv_mac = (TextView)findViewById(R.id.textView_mac);
        tv_ip = (TextView)findViewById(R.id.textView_ip);
        button = (Button)findViewById(R.id.button_operate);

        //获得title
        bt_back = (Button)findViewById(R.id.button_back);
        bt_edit = (Button)findViewById(R.id.button_edit);
        tv_title = (TextView)findViewById(R.id.tv_title);
        bt_back.setText("返回");
        bt_edit.setVisibility(View.INVISIBLE);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获得tokenh和compute_data
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        curcompute = (Computer)intent.getSerializableExtra("compute_data");


        tv_id.setText(curcompute.getId().toString());
        tv_title.setText(curcompute.getComputename().toString());
        tv_state.setText(curcompute.getState().toString());
        tv_mac.setText(curcompute.getMac().toString());
        tv_ip.setText(curcompute.getIp().toString());

        if (curcompute.getState().toString().equals("关机")) {
            button.setText("开机");
            button.setBackgroundResource(R.mipmap.button01);
        }else {
            button.setText("关机");
            button.setBackgroundResource(R.mipmap.buttonred);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals("关机")) {
                    Toast.makeText(Activity_computeDetail.this,"你选择了关机", Toast.LENGTH_SHORT).show();
                    changeState(TO_SHUTDOWN);

                }else {
                    Toast.makeText(Activity_computeDetail.this,"你选择了开机", Toast.LENGTH_SHORT).show();
                    changeState(TO_SETUP);
                }
            }
        });

    }

    /**
     * 改变机器的开关机状态
     * @param tostate
     * @return
     */
    public boolean changeState(final int tostate)
    {

        switch (tostate) {
            case TO_SETUP:
                tostatestr = "开机";
                address = Hostname.hostname + "openip/" + token + "/" + curcompute.getIp().toString() + "/";
                break;
            case TO_SHUTDOWN:
                tostatestr = "关机";
                address = Hostname.hostname + "closeip/" + token + "/" + curcompute.getIp().toString() + "/";
                break;
            default:
                break;
        }

        boolean res = HttpUtil.sendHttpRequest(Activity_computeDetail.this, address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Activity_computeDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_computeDetail.this, tostatestr+"成功", Toast.LENGTH_SHORT).show();
                        if (tostatestr.equals("关机")) {
                            button.setText("开机");
                            tv_state.setText("关机");
                            button.setBackgroundResource(R.mipmap.button01);
                        }else {
                            button.setText("关机");
                            tv_state.setText("开机");
                            button.setBackgroundResource(R.mipmap.buttonred);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Activity_computeDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_computeDetail.this, tostatestr+"失败1", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNotFind(final int rescode) {
                Activity_computeDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(rescode/100 == 4)
                            Toast.makeText(Activity_computeDetail.this, "连接超时关机失败", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Activity_computeDetail.this, "服务器内部错误", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


}
