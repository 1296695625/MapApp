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


public class LocalService extends Service {
    private ServiceConnection connection;
    private MyService localService;

    @Override
    public IBinder onBind(Intent intent) {
        return localService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (null == connection) {
            connection = new LocalConnection();
        }
        localService = new MyService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("tfhr","localservice ssart");
        Intent intent1=new Intent();
        intent1.setClass(this,RemoteService.class);
        bindService(intent1,connection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class MyService extends MyAidlService.Stub {
        @Override
        public String getName() throws RemoteException {
            return "local";
        }
    }

    class LocalConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("tfhr","connect to remote");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("tfhr","disconnect remote");
            Intent reconnect=new Intent();
            LocalService.this.startService(new Intent(LocalService.this,RemoteService.class));
//            LocalService.this.bindService(new Intent(LocalService.this,RemoteService.class),connection,Context.BIND_IMPORTANT);
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }
    }
}
