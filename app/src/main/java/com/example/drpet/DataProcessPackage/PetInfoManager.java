package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigInteger;


/**
 * Created by Administrator on 2016/5/28 0028.
 */
public class PetInfoManager {
    private DbHelper mydbhelper;
    public PetInfoManager(Context context,String name,int version){
        mydbhelper = new DbHelper(context,name,null,version);
    }

    /***
     *  插入宠物信息数据 参数如命
     * @param time          当前系统时间
     * @param happyValue    。
     * @param healthValue   。
     * @param hungerValue   。
     */
    public void insert(long time, int happyValue, int healthValue, int hungerValue, String desc){
        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        db.execSQL("insert into petinfo(_time,HappyValue,HealthValue,HungerValue,desc)values(" +
                "'"+String.valueOf(time)+"'," +
                "'"+String.valueOf(happyValue)+"'," +
                "'"+String.valueOf(healthValue)+"'," +
                "'"+String.valueOf(hungerValue)+"'," +
                "'"+desc+"')" );
        db.close();
        deleteOldInfo();
    }

    /***
     * 查询宠物信息
     * @return 数据库查询结果Cursor
     */
    public Cursor getPetInfo(){
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        Cursor cursor = db.query("petinfo", null, null, null, null, null, null, null);
        return cursor;
    }
    public void closeCursor(Cursor cursor){
        if(!cursor.isClosed())
            cursor.close();
    }

    /***
     * 查询上次操作时间
     * @return  时间
     */
    public long getLastTime(){
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        Cursor cursor = db.query("petinfo", null, null, null, null, null, null, null);
        if(cursor.moveToLast()){
            String result = cursor.getString(cursor.getColumnIndex("_time"));
            if(result != null) {
                cursor.close();
                return Long.parseLong(result);
            }
        }
        cursor.close();
        return System.currentTimeMillis();
    }

    /***
     * 删除过旧数据
     */
    public void deleteOldInfo(){
        Log.i("DATABASE","Delete old information");
        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        while(db.getPageSize() > 1000){
            String sql = "delete from petinfo where _time=(select min(_time) from petinfo)";
            db.execSQL(sql);
        }
        db.close();
    }

}
