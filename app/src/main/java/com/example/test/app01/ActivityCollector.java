package com.example.test.app01;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动收集和管理
 */
public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<AppCompatActivity>();

    /**
     * 生成一个活动后，将该活动添加到列表
     * @param activity
     */
    public static void addActivity(AppCompatActivity activity){
        activities.add(activity);
    }
    /**
     * 销毁一个活动后，将该活动从列表删除
     * @param activity
     */
    public static void removeActivity(AppCompatActivity activity) {
        if (activities.contains(activity))
            activities.remove(activity);
    }

    /**
     * 销毁列表中的所有活动,当在机器列表中点击注销按钮时
     * 会销毁所有的活动，回到登录界面
     */
    public static void finishall() {
        for(AppCompatActivity ac:activities) {
            if (!ac.isFinishing()){
                ac.finish();
            }
        }
        activities.clear();
    }
}
