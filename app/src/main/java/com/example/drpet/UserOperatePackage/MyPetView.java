package com.example.drpet.UserOperatePackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.drpet.R;

import java.lang.reflect.Field;


/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class MyPetView extends LinearLayout{

    private WindowManager mWindowManager;
    public static int viewWidth;
    public static int viewHeight;
    private int statusBarHeight;
    public MyPetView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_petview, this);
        View view = findViewById(R.id.pet_view_layout);
        viewHeight = view.getLayoutParams().height;
        viewWidth = view.getLayoutParams().width;
        mLayoutParams = new WindowManager.LayoutParams();
    }

    private WindowManager.LayoutParams mLayoutParams;
    private float xPosInView;
    private float yPosInView;
    private float xPosInScreen;
    private float yPosInScreen;
    private float xPosInScreen_Down;
    private float yPosInScreen_Down;

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
                if(xPosInScreen_Down == xPosInScreen && yPosInScreen_Down == yPosInScreen){
                    clickPet();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void clickPet() {
        //TODO: 宠物点击事件
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
