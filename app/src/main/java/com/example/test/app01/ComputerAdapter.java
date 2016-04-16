package com.example.test.app01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Computer类适配器
 * 实现在自定义ListView中的加载
 * 在ListView中显示机器的ID,name,state
 */
public class ComputerAdapter extends BaseAdapter{
    private ArrayList<Computer> mData;
    private Context mContext;
    public ComputerAdapter(ArrayList<Computer> mData, Context mContext) {
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
    public View getView(int Position, View convertView, ViewGroup parent)
    {
        ViewHolder itemHoder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.compute_item, parent, false);
            itemHoder = new ViewHolder();
            itemHoder.txt_computername = (TextView) convertView.findViewById(R.id.tv_computename);
            itemHoder.txt_ip = (TextView) convertView.findViewById(R.id.tv_computeip);
            itemHoder.txt_state = (TextView) convertView.findViewById(R.id.tv_computestate);
            convertView.setTag(itemHoder);
        }else
        {
            itemHoder = (ViewHolder)convertView.getTag();
        }
        itemHoder.txt_computername.setText(mData.get(Position).getComputename());
        itemHoder.txt_ip.setText(mData.get(Position).getIp());
        itemHoder.txt_state.setText(mData.get(Position).getState());

        return convertView;
    }
    private static class ViewHolder{
        TextView txt_computername;
        TextView txt_ip;
        TextView txt_state;
    }
}
