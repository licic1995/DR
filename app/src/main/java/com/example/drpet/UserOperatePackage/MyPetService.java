package com.example.drpet.UserOperatePackage;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class MyPetService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MyPetService(){}

    private Handler handler = new Handler();
    private Timer timer;
    public static boolean isStart = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service","<--ServiceCreate-->");
        MyPetService.isStart = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefresTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        isStart = false;
    }

    private class RefresTask extends TimerTask {
        RefresTask(){}
        @Override
        public void run() {
            //检验是否为桌面是否有悬浮窗显示的响应
            if(isHome() && !MyWindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createPetView(getApplicationContext());
                    }
                });
            }
            else if(!isHome() && MyWindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.removePetView(getApplicationContext());
                    }
                });
            }
            else if(isHome() && MyWindowManager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: Animation
                    }
                });
            }
        }
    }

    /***
     * 判断是否为桌面
     * @return
     */
    private boolean isHome() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHome().contains(rti.get(0).topActivity.getPackageName());
    }
    /***
     *获取桌面应用的应用包名
     * @return 所有包名列表
     */
    private List<String> getHome() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, packageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
