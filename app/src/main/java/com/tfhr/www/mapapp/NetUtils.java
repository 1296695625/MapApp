package com.tfhr.www.mapapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetUtils {
    //获取网络状态
    public static boolean getNetStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != manager) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            info.getSubtypeName();
            Log.v("tfhr","net subtype"+info.getSubtypeName());
            if (null != info && info.isAvailable()) {
                if(info.getDetailedState()== NetworkInfo.DetailedState.CONNECTED){
                    return true;
                }else {
                    Toast.makeText(context,"网络已断开",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "网络已关闭", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
//        Network []networks= manager.getAllNetworks();
//        for(int i=0;i<networks.length;i++){
//        }
    }
}
