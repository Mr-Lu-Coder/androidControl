package com.example.test.app01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;
import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Activity_user extends AppCompatActivity implements AdapterView.OnItemClickListener{

    //about user
    private final int REFRESH_OK = 0;
    private final int HTTP_INIT = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PersonAdapter mAdapter = null;
    private ArrayList<Person> mData = null;
    private ArrayList<Person> tmpData = null;
    private Context mContext;
    private String token = new String("");
    private String TAG = new String("userActivity");

    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_OK:
                    Log.d(TAG, "refresh");
                    tmpData = Utility.handlePersonresponse(msg.obj.toString());
                    boolean should_refresh = false;
                    for (Person person:tmpData) {
                        boolean is_find = false;
                        for (Person mperson:mData) {
                            if (mperson.getId().equals(person.getId())) {
                                is_find = true;
                                break;
                            }
                        }
                        if (!is_find) {
                            mData.add(person);
                            should_refresh = true;
                        }
                    }
                    if (should_refresh)mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "should_refresh"+should_refresh);
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case HTTP_INIT:
                    tmpData = Utility.handlePersonresponse(msg.obj.toString());
                    Log.d(TAG, "add+update");
                    for (Person person:tmpData) {
                        mData.add(person);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获得从上个活动传来的token
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.d(TAG, token);
        Toast.makeText(Activity_user.this, "点击用户取得详细信息", Toast.LENGTH_SHORT).show();
        //获得title
        bt_back = (Button)findViewById(R.id.button_back);
        bt_edit = (Button)findViewById(R.id.button_edit);
        tv_title = (TextView)findViewById(R.id.tv_title);
        bt_back.setText("注销");
        bt_edit.setText("机器");
        tv_title.setText("关于用户");
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_user.this, Activity_login.class);
                startActivity(intent);
                ActivityCollector.finishall();
            }
        });
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_user.this, Activity_group.class);
                intent.putExtra("token", token);
                intent.putExtra("person_data", mData);
                startActivity(intent);
            }
        });

        //对下拉刷新进行监听
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //write here
                Inituserlist(REFRESH_OK);
            }
        });


        mContext = Activity_user.this;
        listView = (ListView)findViewById(R.id.id_listview);

        //初始化user列表
        Inituserlist(HTTP_INIT);
        mData = new ArrayList<Person>();
        mData.add(new Person("ID", "名字", "身份"));
        mAdapter = new PersonAdapter((ArrayList<Person>) mData, mContext);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    public void Inituserlist(final int STATE) {
        Log.d(TAG, "curThread"+Thread.currentThread());
        String adress = Hostname.hostname + "user/" + token + "/";
        HttpUtil.sendHttpRequest(Activity_user.this, adress,
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

                    }

                    @Override
                    public void onNotFind() {

                    }


                }
        );
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, "你点击了第" + position  + "项", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.setting_add01:
                Toast.makeText(Activity_user.this, "注册用户", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settin_add02:
                Toast.makeText(Activity_user.this, "添加机器", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
