package com.tfhr.www.mapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> datas;

    public MyAdapter(Context c) {
        this.context = c;
        datas = new ArrayList<>();
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");
        datas.add("5");
        datas.add("5");
        datas.add("5");
        datas.add("5");
        datas.add("5");
        datas.add("5");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("6");
        datas.add("7");
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            holder=new ViewHolder();
            holder.tv=convertView.findViewById(R.id.item_tv);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(datas.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
