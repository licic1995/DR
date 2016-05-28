package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/25 0025.
 */


public class DbHelper extends SQLiteOpenHelper{

    /***
     * DateBase: petinfo
     *      _time       记录时间
     *      HappyValue  快乐值
     *      HealthValue 健康值
     *      HungerValue 饥饿值v
     */
    private static final int VERSION = 1;


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //TODO:DbHelper
    //

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS petinfo" +
                "(_time INTEGER PRIMARY KEY NOT NULL," +
                "HappyValue INTEGER NOT NULL," +
                "HealthValue INTEGER NOT NULL," +
                "HungerValue INTEGER NOT NULL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
