package com.example.drpet.UserOperatePackage;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.drpet.AnimationPackage.AnimationPlayer;
import com.example.drpet.DataProcessPackage.PetInfo;
import com.example.drpet.R;

import java.lang.reflect.Field;
import java.util.Calendar;


/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class MyPetView extends LinearLayout{

    private WindowManager mWindowManager;
    public static int viewWidth;
    public static int viewHeight;
    private int statusBarHeight;
    private volatile int flag = 0;
    private Calendar calendar;

    private Handler handler;//次handler更新ui依然会报错   =。=  尴尬
    private Thread sleepthread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while(flag<10){
                    Thread.currentThread().sleep(300);
                    flag++;
                    System.out.println(flag);
                }
//                Message msg = new Message();
//                msg.what = 1;
//                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    public MyPetView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_petview, this);
        View view = findViewById(R.id.pet_view_layout);
        viewHeight = view.getLayoutParams().height;
        viewWidth = view.getLayoutParams().width;

    }

    private WindowManager.LayoutParams mLayoutParams;
    private float xPosInView;
    private float yPosInView;
    private float xPosInScreen;
    private float yPosInScreen;
    private float xPosInScreen_Down;
    private float yPosInScreen_Down;

    public void setmLayoutParams(WindowManager.LayoutParams params){
        mLayoutParams = params;
    }

    /***
     * 响应宠物点击拖动事件
     * @param event 点击事件参数
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xPosInView = event.getX();
                yPosInView = event.getY();
                xPosInScreen_Down = event.getRawX();
                yPosInScreen_Down = event.getRawY() - getStatusBarHeight(); //减去状态栏高度
                xPosInScreen = event.getRawX();
                yPosInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xPosInScreen = event.getRawX();
                yPosInScreen = event.getRawY() - getStatusBarHeight();
                updataViewPosion();
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(xPosInScreen_Down - xPosInScreen) < 20 && Math.abs(yPosInScreen_Down - yPosInScreen) < 20){
                    clickPet();
                }else{
                    moveOver();
                }
                PetInfo.newHappy();
                Log.i("HAPPY VALUE",String.valueOf(PetInfo.getHappy()));
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void moveOver(){
        Log.i("PET","<--------------Move Over----------->");
        AnimationPlayer animationPlayer = new AnimationPlayer((ImageView) findViewById(R.id.iv_desktop));
        animationPlayer.moveeOver();
    }
    private void clickPet() {
        calendar = Calendar.getInstance();
        Log.i("PET","<--------------Click the pet----------->" + String.valueOf(calendar.get(Calendar.HOUR)));
        if(calendar.get(Calendar.HOUR_OF_DAY) < 14 && calendar.get(Calendar.HOUR_OF_DAY) >= 11){
            PetInfo.newHunger();
            AnimationPlayer animationPlayer = new AnimationPlayer((ImageView) findViewById(R.id.iv_desktop));
            animationPlayer.startEat();
            Log.i("PET","Eating");
        }
        else {
            AnimationPlayer animationPlayer = new AnimationPlayer((ImageView) findViewById(R.id.iv_desktop));
            animationPlayer.startSleep();
        }
    }

    /***
     * 更新宠物位置
     */
    private void updataViewPosion() {
        mLayoutParams.x = (int) (xPosInScreen - xPosInView);
        mLayoutParams.y = (int) (yPosInScreen - yPosInView);
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    /***
     * 获取状态栏高度
     * @return statusBarHeight 状态栏高度
     */
    private int getStatusBarHeight(){
        if(statusBarHeight == 0){
            try{
                Class<?> c= Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
