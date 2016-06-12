package com.example.drpet.AlarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.example.drpet.AlarmActivity;
import com.example.drpet.DataProcessPackage.FileHelper;
import com.example.drpet.MainActivity;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BORCAST","Intent cast.........");
        Intent i=new Intent(context, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
