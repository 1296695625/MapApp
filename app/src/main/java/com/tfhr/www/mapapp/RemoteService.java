package com.tfhr.www.mapapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class RemoteService extends Service {
    private MyRemoteS service;
    private RemoteConnection connection;
    @Override
    public void onCreate() {
        super.onCreate();
        if(null==connection){
            connection=new RemoteConnection();
        }
        service=new MyRemoteS();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("tfhe","connect to local");
        Intent remote=new Intent();
        remote.setClass(this,LocalService.class);
        bindService(remote,connection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return service;
    }
    class MyRemoteS extends MyAidlService.Stub{
        @Override
        public String getName() throws RemoteException {
            return "remote";
        }
    }
    class RemoteConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("tfhr","connect to local ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
//            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),connection,Context.BIND_IMPORTANT);
        }
    }
}
