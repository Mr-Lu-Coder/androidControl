package com.example.test.app01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lushangqi on 2016/3/26.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {
    private ArrayList<Person> gData;
    private ArrayList<ArrayList<Computer>> iData;
    private Context mContext;
    public MyExpandableAdapter (ArrayList<Person> gData, ArrayList<ArrayList<Computer>> iData, Context mContext)
    {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount()
    {return  gData.size();}
    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Person getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Computer getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_item, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.txt_id = (TextView)convertView.findViewById(R.id.tv_userid);
            groupHolder.txt_Name = (TextView)convertView.findViewById(R.id.tv_uname);
            groupHolder.txt_Role = (TextView)convertView.findViewById(R.id.tv_urole);
            convertView.setTag(groupHolder);
        }
        else {
            groupHolder = (ViewHolderGroup)convertView.getTag();
        }
        groupHolder.txt_id.setText(gData.get(groupPosition).getId());
        groupHolder.txt_Name.setText(gData.get(groupPosition).getName());
        groupHolder.txt_Role.setText(gData.get(groupPosition).getRole());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {
        ViewHolderItem itemHoder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.compute_item, parent, false);
            itemHoder = new ViewHolderItem();
            itemHoder.txt_computername = (TextView) convertView.findViewById(R.id.tv_computename);
            itemHoder.txt_ip = (TextView) convertView.findViewById(R.id.tv_computeip);
            itemHoder.txt_state = (TextView) convertView.findViewById(R.id.tv_computestate);
            convertView.setTag(itemHoder);
        }else
        {
            itemHoder = (ViewHolderItem)convertView.getTag();
        }
        itemHoder.txt_computername.setText(iData.get(groupPosition).get(childPosition).getComputename());
        itemHoder.txt_ip.setText(iData.get(groupPosition).get(childPosition).getIp());
        itemHoder.txt_state.setText(iData.get(groupPosition).get(childPosition).getState());

        return convertView;
    }

    private static class ViewHolderGroup{
        TextView txt_id;
        TextView txt_Name;
        TextView txt_Role;

    }

    private static class ViewHolderItem{
        TextView txt_computername;
        TextView txt_ip;
        TextView txt_state;
    }

}
