package com.tfhr.www.mapapp;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;

public class NavigateGuidActivity extends Activity {
    private WalkNavigateHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_navigate_guid);
        helper = WalkNavigateHelper.getInstance();
        View view = helper.onCreate(NavigateGuidActivity.this);
        if (null != view) {
            setContentView(view);
        }
        helper.startWalkNavi(NavigateGuidActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.quit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.pause();
    }
}
