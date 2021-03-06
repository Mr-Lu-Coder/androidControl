package com.example.test.app01;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Person类适配器
 * 实现在自定义ListView中的加载
 * 在ListView中显示机器的ID,name,role
 */

public class PersonAdapter extends BaseAdapter {

    private ArrayList<Person> mData;
    private Context mContext;
    public PersonAdapter(ArrayList<Person> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertview == null) {
            convertview = LayoutInflater.from(mContext).inflate(R.layout.person_item, parent, false);
            holder = new ViewHolder();
            holder.txt_id = (TextView)convertview.findViewById(R.id.tv_userid);
            holder.txt_Name = (TextView)convertview.findViewById(R.id.tv_uname);
            holder.txt_Role = (TextView)convertview.findViewById(R.id.tv_urole);
            convertview.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertview.getTag();
        }
        holder.txt_id.setText(mData.get(position).getId());
        holder.txt_Name.setText(mData.get(position).getName());
        holder.txt_Role.setText(mData.get(position).getRole());
        return convertview;
    }
    private class ViewHolder{
        TextView txt_id;
        TextView txt_Name;
        TextView txt_Role;
    }
}
