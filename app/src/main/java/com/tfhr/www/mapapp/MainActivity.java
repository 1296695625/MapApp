package com.tfhr.www.mapapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button map, drawlayout;
    private ListView listView;
    private MyAdapter adapter;
    float starty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.jumpmap);
        listView = findViewById(R.id.listview);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
        drawlayout = findViewById(R.id.drawlayout);
        drawlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDoubleService();
//                startActivity(new Intent(MainActivity.this, DrawLayoutActivity.class));
            }
        });
        if (null == adapter) {
            adapter = new MyAdapter(this);
        }
        LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        final LinearLayout l = ll.findViewById(R.id.layout);
        ProgressBar progressBar = ll.findViewById(R.id.progressbar);
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.v("tfhr", "scroll -- " + scrollState);
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.v("tfhr", "onscroll -" + firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);
//                if (firstVisibleItem == 0 && listView.getHeaderViewsCount()==0) {
//                    listView.addHeaderView(ll);
//                }
//            }
//        });
        listView.addHeaderView(ll);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean flag = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        starty = event.getY();
//                        listView.removeHeaderView(ll);
//                        return true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (v.getTop() == 201 && event.getY() - starty > 200 && l.getVisibility() == View.GONE) {
                            Log.v("tfhr", "move" + (event.getRawY() - starty));
                            l.setVisibility(View.VISIBLE);
                        } else {
//                            listView.removeHeaderView(ll);
                            l.setVisibility(View.GONE);
                        }
//                            flag= true;
//                            Log.v("tfhr","move1"+event.getRawY()+"-"+event.getY()+"-"+v.getTop()+"-"+(event.getRawY() - starty));
//                        return false;
                        break;
//
                    default:
                        flag = false;
                }
                return flag;
            }
        });
    }

    private void initDoubleService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startService(new Intent(this, MyJobService.class));
            Log.v("tfhr","init version >= lollipop");
            return;
        } else {
            Log.v("tfhr","init version < lollipop");
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
    }
}
