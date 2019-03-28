package com.tfhr.www.mapapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyPopmenu extends PopupWindow {
    String names[];
    private Context c;
    private int id = R.layout.item_pop;
    private View view, dropDownView;
    private PopItemClickListener listener;

    public MyPopmenu(Context context) {
        this.c = context;
        this.listener = (PopItemClickListener) context;
//        initView();
    }

    public void setDropDownView(View dropDownView) {
        this.dropDownView = dropDownView;
    }

    PopupWindow popupWindow;

    public void initView() {
        if (null == view) {
            view = LayoutInflater.from(c).inflate(id, null);
        }
        if (null == view) {
            return;
        }
        popupWindow = new PopupWindow();
        popupWindow.setWidth(200);
        popupWindow.setHeight(200);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        ListView lv = view.findViewById(R.id.pop_listview);
        lv.setAdapter(new PopLisviewAdapter());
        popupWindow.setContentView(view);
        if (null != dropDownView) {
            popupWindow.showAsDropDown(dropDownView);
        }else {
            try {
                throw new Exception("未设置dropdown view");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isShow(){
        if(null!=popupWindow){
            return popupWindow.isShowing();
        }
        return false;
    }
    public void dismiss(){
        if(null!=popupWindow){
            popupWindow.dismiss();
        }
    }
    public void setData(String[] strs) {
        this.names = strs;
    }

    public void setView(int id) {
        this.id = id;
    }

    public void setView(View v) {
        this.view = v;
    }

    interface PopItemClickListener {
        void popItemClick(int position, String name);
    }

    class PopLisviewAdapter extends BaseAdapter {
        public PopLisviewAdapter() {
        }

        @Override
        public int getCount() {
            if (null != names) {
                return names.length;
            }
            return 0;
        }

        @Override
        public String getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(c).inflate(R.layout.item_layout, parent, false);
                holder = new ViewHolder();
                holder.textView=convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText("" + names[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    listener.popItemClick(position, names[position]);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
