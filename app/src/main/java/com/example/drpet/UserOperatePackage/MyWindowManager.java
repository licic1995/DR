package com.example.drpet.UserOperatePackage;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class MyWindowManager {
    private static MyPetView mPetView;
    private static WindowManager mWindowManager;
    private static LayoutParams petLayoutParams;

    /***
     * 检测是否有宠物窗口显示
     * @return boolean
     */
    public static boolean isWindowShowing() {
        return mPetView!=null;
    }


    /***
     * 开启桌面宠物
     * @param context 上下文
     */
    public static void createPetView(Context context) {
        WindowManager windowmanager = getWindowManager(context);
        DisplayMetrics dm = new DisplayMetrics();
        windowmanager.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        if(mPetView == null){
            mPetView = new MyPetView(context);
            if(petLayoutParams == null){
                petLayoutParams = new LayoutParams();
                petLayoutParams.type = LayoutParams.TYPE_PHONE;
                petLayoutParams.format = PixelFormat.RGBA_8888;//背景透明
                petLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                petLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                petLayoutParams.width = MyPetView.viewWidth;
                petLayoutParams.height = MyPetView.viewHeight;
                petLayoutParams.x = screenWidth;
                petLayoutParams.y = screenHeight / 2;//初始化位置在屏幕右边缘中间处
            }
            mPetView.setLayoutParams(petLayoutParams);
            windowmanager.addView(mPetView, petLayoutParams);
        }


    }

    /***
     * 移除宠物界面
     * @param context ..
     */
    public static void removePetView(Context context) {
        if(mPetView != null){
            WindowManager windowmanager = getWindowManager(context);
            windowmanager.removeView(mPetView);
            mPetView = null;
        }
    }

    /***
     * 获取当前WindowManger，若无则创建WindowManager返回
     * @param context 当前应用程序的context
     * @return 当前WindowManager实例
     */
    private static WindowManager getWindowManager(Context context) {
        if(mWindowManager == null){
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
