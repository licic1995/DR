package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.util.Log;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class PetInfo {
    public static PetInfoManager petInfoManager;
    protected static int happy,health,hunger;
    public PetInfo(Context context, String name){
        petInfoManager = new PetInfoManager(context, name, 1);
        initValue();
    }
    public static void initManager(Context context, String name){
        petInfoManager = new PetInfoManager(context, name, 1);
        initValue();
    }
    private static void initValue(){
        Cursor cursor = petInfoManager.getPetInfo();

        Log.i("cursor",cursor.toString());
        if(cursor == null || !cursor.moveToLast()){
            Log.i("cursor","null");
            happy = 50;
            hunger = 50;
            health = 50;
            petInfoManager.insert(System.currentTimeMillis(), happy,health,hunger,"init");
            cursor.close();
            return;
        }
        if(cursor.getString(cursor.getColumnIndex("HappyValue")) != null)
            happy = Integer.parseInt(cursor.getString(cursor.getColumnIndex("HappyValue")));
        else
            happy = 100;
        if(cursor.getString(cursor.getColumnIndex("HungerValue")) != null)
            hunger = Integer.parseInt(cursor.getString(cursor.getColumnIndex("HungerValue")));
        else
            hunger = 100;
        if(cursor.getString(cursor.getColumnIndex("HealthValue")) != null)
            health = Integer.parseInt(cursor.getString(cursor.getColumnIndex("HealthValue")));
        else
            health = 100;
        cursor.close();
    }
    public static void updata(){
        long nowtime = System.currentTimeMillis();
        long lasttime = petInfoManager.getLastTime();
        long time = nowtime - lasttime;
        int hunger_now = hunger - (int)(time/3600000.0/8*2/3*hunger); // 平均八小时减少1/3。
        int health_now = (health + (int)( time * 10.0 / 3600000));//平均一小时恢复十点
        int happy_now = happy - (int)(( time + 1800000)/3600000);//平均一小时减少一点
        String tip = "Open";
        petInfoManager.insert(nowtime , happy_now, health_now ,hunger_now, tip);
        happy = happy_now;
        health = health_now;
        hunger = hunger_now;
    }

    public static void newHunger(){
        hunger = Math.min(hunger + 30 , 100);
    }
    public static void newHappy(){
        happy = Math.min(happy + 2 , 100);
    }
    public static void newHealth() {
        health = Math.max(health - 1, 0);
    }
    public static void saveAndClose(){
        String tip = "Close";
        long nowtime = System.currentTimeMillis();
        petInfoManager.insert(nowtime , happy, health ,hunger, tip);
    }

    public static int getHappy() {
        return happy;
    }

    public static int getHealth() {
        return health;
    }

    public static int getHunger() {
        return hunger;
    }

    public static void testInfo(){
        happy = happy / 2;
        hunger = hunger / 2;
        health = health / 2;
        petInfoManager.insert(System.currentTimeMillis(), happy,health,hunger,"test");
    }
}
