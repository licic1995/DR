package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


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
    public void insert(int time, int happyValue, int healthValue, int hungerValue){
        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        db.execSQL("insert into petinfo(_time,HappyValue,HealthValue,HungerValue)values(" +
                "'"+time+"'," +
                "'"+happyValue+"'," +
                "'"+healthValue+"'," +
                "'"+hungerValue+"')" );
        db.close();
    }

    /***
     * 查询宠物信息
     * @return 数据库查询结果Cursor
     */
    public Cursor getPetInfo(){
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        Cursor cursor = db.query("petinfo", null, null, null, null, null, null);
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
    public int getLastTime(){
        SQLiteDatabase db = mydbhelper.getReadableDatabase();
        Cursor cursor = db.query("petinfo", null, null, null, null, null, null);
        if(cursor.moveToLast()){
            String result = cursor.getString(cursor.getColumnIndex("_time"));
            return Integer.parseInt(result);
        }
        cursor.close();
        return 0;
    }

    /***
     * 删除过旧数据
     */
    public void deleteOldInfo(){
        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        while(db.getPageSize() > 800){
            String sql = "delete from petinfo where _time=(select min(_time) from petinfo)";
            db.execSQL(sql);
        }
        db.close();
    }
}
