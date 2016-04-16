package com.example.test.app01;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * 机器列表
 */
public class Activity_compute extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private final int HTTP_REFRESH = 0;
    private final int HTTP_INIT = 1;
    private final int HTTP_ERROR = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ComputerAdapter mAdapter = null;
    private ArrayList<Computer> mData = null;
    private ArrayList<Computer> tmpData = null;
    private Context mContext;
    private String token = new String("");
    private String TAG = new String("computeActivity");

    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;
    /**
     * 对异步消息进行处理
     */
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case HTTP_REFRESH:
                    Log.d(TAG, "refresh");
                    tmpData = Utility.handleComputeresponse(msg.obj.toString());
                    boolean should_refresh = false;
                    for (Computer tmpcomputer:tmpData) {
                        boolean is_find = false;
                        for (Computer computer:mData) {
                            if (computer.getId().equals(tmpcomputer.getId())) {
                                is_find = true;
                                if (!computer.equals(tmpcomputer)) {
                                    should_refresh = true;
                                    mData.remove(computer);
                                    //替换掉
                                    mData.add(tmpcomputer);
                                    //writehere
                                }else {

                                }
                                break;
                            }
                        }
                        if (!is_find) {
                            mData.add(tmpcomputer);
                            should_refresh = true;
                        }
                    }
                    if (should_refresh){
                        Log.d(TAG, "sort");
                        Collections.sort(mData, new SortByID());
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(Activity_compute.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Activity_compute.this, "没有新增的机器", Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "should_refresh"+should_refresh);
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case HTTP_INIT:
                    tmpData = Utility.handleComputeresponse(msg.obj.toString());
                    Log.d(TAG, "add+update");
                    for (Computer computer:tmpData) {
                        mData.add(computer);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(Activity_compute.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute);
        ActivityCollector.addActivity(this);
        //获得从上个活动传来的token
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.d(TAG, token);
        Toast.makeText(Activity_compute.this, "点击查看详细信息", Toast.LENGTH_SHORT).show();

        //获得title
        bt_back = (Button)findViewById(R.id.button_back);
        bt_edit = (Button)findViewById(R.id.button_edit);
        tv_title = (TextView)findViewById(R.id.tv_title);
        bt_back.setText("注销");
        bt_edit.setText("添加");
        tv_title.setText("机器列表");

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_compute.this, Activity_login.class);
                startActivity(intent);
                ActivityCollector.finishall();
            }
        });
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_compute.this, Activity_addCompute.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });


        //对下拉刷新进行监听
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_compute_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //write here
                Initcomputerlist(HTTP_REFRESH);
            }
        });


        mContext = Activity_compute.this;

        listView = (ListView)findViewById(R.id.id_compute_listview);
        //初始化compute列表
        Initcomputerlist(HTTP_INIT);
        mData = new ArrayList<Computer>();
        mAdapter = new ComputerAdapter((ArrayList<Computer>) mData, mContext);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        //当列表为空时显示为空

    }

    /**
     * 刷新机器列表
     * @param STATE
     */
    public void Initcomputerlist(final int STATE) {
        Log.d(TAG, "curThread"+Thread.currentThread());
        String adress = Hostname.hostname + "compute/" + token + "/";
        HttpUtil.sendHttpRequest(Activity_compute.this, adress,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Log.d(TAG+"http", "curThread"+Thread.currentThread());
                        //Log.d(TAG, response);
                        Message message = new Message();
                        message.what = STATE;
                        message.obj = response;
                        mHandler.sendMessage(message);

                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.what = HTTP_ERROR;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onNotFind(final int rescode) {
                        Message message = new Message();
                        message.what = HTTP_ERROR;
                        mHandler.sendMessage(message);
                    }


                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Computer curcomputer = mData.get(position);
        Intent intent = new Intent(Activity_compute.this, Activity_computeDetail.class);
        intent.putExtra("token", token);
        intent.putExtra("compute_data", curcomputer);
        Log.d(TAG, curcomputer.getComputename().toString());
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

/**
 * 对列表机器中按ID从小到大排序
 */
class SortByID implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        Computer c1 = (Computer)o1;
        Computer c2 = (Computer)o2;
        if (Double.parseDouble(c1.getId()) < Double.parseDouble(c2.getId())) {
            return -1;
        }else {
            return 1;
        }
    }
}