package com.tfhr.www.mapapp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrawLayoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout drawlayout;
    private ListView leftDrawer;
    private ArrayList<String> strs=new ArrayList<>();
    private TextView bt;
//    private ActionBarDrawerToggle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_layout);
        drawlayout=findViewById(R.id.drawlayout);
        bt=findViewById(R.id.bottomtv);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        leftDrawer=findViewById(R.id.left_layout);
        Map map=new HashMap();
        strs.add("111");
        strs.add("222");
        strs.add("333");
        strs.add("444");
        leftDrawer.setAdapter(new ArrayAdapter(this,R.layout.item_layout,R.id.item_tv,strs));
        leftDrawer.setOnItemClickListener(this);
        drawlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                visibleListView();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                hideListView();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });
    }

    private void hideListView() {
        if(null!=leftDrawer){
            leftDrawer.setVisibility(View.VISIBLE);
        }
    }

    private void visibleListView() {
        if(null!=leftDrawer){
            leftDrawer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("tfhr","item click "+strs.get(position));
    }

    class MyDrawItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //点击item后操作
        }
    }
}
