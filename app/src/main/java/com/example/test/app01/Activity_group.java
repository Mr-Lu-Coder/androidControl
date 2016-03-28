package com.example.test.app01;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;

import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class Activity_group extends AppCompatActivity {
    private String TAG = new String("groupActivity");
    private final int GROUP_REFRESH = 0;
    private final int GROUP_INIT = 1;
    private final int GROUP_ERROR = 2;
    private SwipeRefreshLayout swipegroupRefreshLayout;
    private ExpandableListView expandableListView;
    private MyExpandableAdapter myExpandableAdapter;
    private ArrayList<Person> personData = null;
    private ArrayList<Person> tmppersonData = null;
    private ArrayList<ArrayList<Computer>> computersData = null;
    private ArrayList<Computer> tmpcomputersData = null;
    private ArrayList<Confirm> confirmData;
    private ArrayList<Confirm> tmpconfirmData;
    private String token;
    private Context mContext;

    //Title
    private Button bt_back;
    private Button bt_edit;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "creat");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ActivityCollector.addActivity(this);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.d(TAG, token);
        //获得从user活动传来的所有的person
        personData = (ArrayList<Person>)intent.getSerializableExtra("person_data");
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
        bt_edit.setVisibility(View.INVISIBLE);
        tv_title.setText("关于机器");
        Toast.makeText(this, "点击机器取得详细信息", Toast.LENGTH_SHORT).show();
        swipegroupRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_swiperefresh_group);
        swipegroupRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //write here
                //Inituserlist(REFRESH_OK);
            }
        });

        expandableListView = (ExpandableListView)findViewById(R.id.expandablelist);
        mContext = Activity_group.this;
        computersData = new ArrayList<ArrayList<Computer>>();
        //personData.add(new Person("ID", "名字", "身份"));
        //personData.add(new Person("one", "asdfasd", "管理员"));
        tmpcomputersData = new ArrayList<Computer>();
        tmpcomputersData.add(new Computer("huipu", "1.1.1.1", "开机"));
        tmpcomputersData.add(new Computer("huipu", "1.1.1.1", "开机"));
        tmpcomputersData.add(new Computer("huipu", "1.1.1.1", "开机"));
        tmpcomputersData.add(new Computer("huipu", "1.1.1.1", "开机"));
        computersData.add(tmpcomputersData);
        tmpcomputersData = new ArrayList<Computer>();
        tmpcomputersData.add(new Computer("thikpad", "3.3.3.3", "开机"));
        tmpcomputersData.add(new Computer("thikpad", "3.3.3.3", "开机"));
        tmpcomputersData.add(new Computer("thikpad", "3.3.3.3", "开机"));
        computersData.add(tmpcomputersData);



        myExpandableAdapter = new MyExpandableAdapter(personData, computersData, mContext);
        expandableListView.setAdapter(myExpandableAdapter);
        expandableListView.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent,
                                        View v, int groupPosition, long id) {
                Log.d(TAG, groupPosition+"+"+id);
                Log.d(TAG, computersData.size()+"");
                if (computersData != null && computersData.size() >= groupPosition+1 &&
                         computersData.get(groupPosition).size()>0)
                    return false;
                else {
                    Toast.makeText(Activity_group.this, "该用户暂时没有管理机器", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        });
        expandableListView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                int childPosition, long i){
                Toast.makeText(Activity_group.this,
                        "你点击了"+computersData.get(groupPosition).get(childPosition).getComputename(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }



    public void groupListInit(final int state) {
        switch (state) {
            case GROUP_INIT:

                break;
            case GROUP_REFRESH:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
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
                Toast.makeText(this, "注册用户", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settin_add02:
                Toast.makeText(this, "添加机器", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
