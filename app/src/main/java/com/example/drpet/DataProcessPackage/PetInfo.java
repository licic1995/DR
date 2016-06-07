package com.example.drpet.DataProcessPackage;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class PetInfo {
    PetInfoManager petInfoManager;
    PetInfo(Context context, String name){
        petInfoManager = new PetInfoManager(context, name, 1);
    }

    public void updata(int happy, int health, int hunger){
        long now = System.currentTimeMillis();
        String tip;
//        petInfoManager.insert(now , happy, health ,hunger, );
    }
}
