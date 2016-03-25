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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;
import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private final int REFRESH_OK = 0;
    private final int HTTP_INIT = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PersonAdapter mAdapter = null;
    private List<Person> mData = null;
    private List<Person> tmpData = null;
    private Context mContext;
    private TextView tv_motion;
    private String token = new String("");
    private String TAG = new String("MainActivity");

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_OK:
                    Log.d(TAG, "refresh");
                    ParseJsonwithGson(msg.obj.toString());
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
                    ParseJsonwithGson(msg.obj.toString());
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_motion = (TextView)findViewById(R.id.tv_motion);
        //获得从上个活动传来的token
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.d(TAG, token);
        //对下拉刷新进行监听
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //write here
                Inituserlist(REFRESH_OK);
            }
        });


        mContext = MainActivity.this;
        listView = (ListView)findViewById(R.id.id_listview);

        //初始化user列表
        Inituserlist(HTTP_INIT);
        mData = new LinkedList<Person>();
        mData.add(new Person("ID", "名字", "身份"));
        Log.d(TAG, "mData" + mData.size() +tv_motion.getText().toString());
        if (mData.size() > 0) tv_motion.setVisibility(View.INVISIBLE);
        mAdapter = new PersonAdapter((LinkedList<Person>) mData, mContext);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

    }
    public void Inituserlist(final int STATE) {
        Log.d(TAG, "curThread"+Thread.currentThread());
        String adress = Hostname.hostname + "user/" + token + "/";
        HttpUtil.sendHttpRequest(adress,
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

    public void ParseJsonwithGson(String jsonData) {
        Log.d(TAG+"json", "curThread"+Thread.currentThread());
        Gson gson = new Gson();
        tmpData = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
        for (Person person:mData) {
            Log.d(TAG, "id is" + person.getId());
        }
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
}
