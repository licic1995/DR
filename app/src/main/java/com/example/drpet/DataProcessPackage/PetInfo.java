package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class PetInfo {
    PetInfoManager petInfoManager;
    int happy,health,hunger;
    PetInfo(Context context, String name){
        petInfoManager = new PetInfoManager(context, name, 1);
        initValue();

    }
    private void initValue(){
        Cursor cursor = petInfoManager.getPetInfo();
        cursor.moveToLast();
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
    public void updata(){
        long nowtime = System.currentTimeMillis();
        long lasttime = petInfoManager.getLastTime();
        long time = nowtime - lasttime;
        int hunger_now = hunger - (int)(time/3600000.0/8*2/3*hunger); // 每八小时减少1/3。
        int health_now = (health );//TODO: Algorithm of value health
        int happy_now = happy - (int)(( time + 1800000)/3600000);
        String tip = "Open";
        petInfoManager.insert(nowtime , happy_now, health_now ,hunger_now, tip);
        happy = happy_now;
        health = health_now;
        hunger = hunger_now;
    }

    public void newHunger(){
        hunger = Math.min(hunger + 30 , 100);
    }
    public void newHappy(){
        happy = Math.min(happy + 2 , 100);
    }
    public void saveAndClose(){
        String tip = "Close";
        long nowtime = System.currentTimeMillis();
        petInfoManager.insert(nowtime , happy, health ,hunger, tip);
    }

}
