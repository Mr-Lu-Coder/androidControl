package com.example.test.app01;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lushangqi on 2016/3/27.
 */
public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<AppCompatActivity>();
    public static void addActivity(AppCompatActivity activity){
        activities.add(activity);
    }
    public static void removeActivity(AppCompatActivity activity) {
        if (activities.contains(activity))
            activities.remove(activity);
    }
    public static void finishall() {
        for(AppCompatActivity ac:activities) {
            if (!ac.isFinishing()){
                ac.finish();
            }
        }
        activities.clear();
    }
}
