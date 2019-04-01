package com.tfhr.www.mapapp;

import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MyJobService extends JobService {
    private JobScheduler jobScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(1, new ComponentName(getPackageName(), MyJobService.class.getName()));
        jobInfoBuilder.setPeriodic(5);
        jobInfoBuilder.setPersisted(true);
        jobInfoBuilder.setRequiresCharging(true);
//        jobScheduler.schedule(jobInfoBuilder.build());
        Log.v("tfhr","jobservice oncreate");
        if (jobScheduler.schedule(jobInfoBuilder.build())<=0) {
            Log.v("tfhr","schedule fail");
        } else {
            Log.v("tfhr","schedule success");
        }
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(this, "服务启动 jobschedule", Toast.LENGTH_SHORT).show();
//        || isServiceRunning(this, "com.ph.myservice.RemoteService") == false
//        System.out.println("开始工作");
        Log.v("tfhr","start to work");
//        if (!isServiceRunning(getApplicationContext(), "com.ph.myservice") || !isServiceRunning(getApplicationContext(), "com.ph.myservice:remote")) {
//            startService(new Intent(this, LocalService.class));
//            startService(new Intent(this, RemoteService.class));
//        }

       /* boolean serviceRunning = isServiceRunning(getApplicationContext(), "com.ph.myservice");
        System.out.println("进程一" + serviceRunning);

        boolean serviceRunning2 = isServiceRunning(getApplicationContext(), "com.ph.myservice:remote");
        System.out.println("进程二" + serviceRunning2);*/
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (!isServiceRunning(this, "com.ph.myservice.LocalService") || !isServiceRunning(this, "com.ph.myservice.RemoteService")) {
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
        return false;
    }
    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();


        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
//            System.out.println(info.processName);
            Log.v("tfhr","running service name"+info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;

    }
}
