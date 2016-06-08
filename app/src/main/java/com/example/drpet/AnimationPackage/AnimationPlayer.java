package com.example.drpet.AnimationPackage;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.example.drpet.R;

/**
 * Created by lichengchen on 16/6/2.
 */
public class AnimationPlayer {
    AnimationDrawable animationDrawable;
    ImageView imageView;
    public AnimationPlayer(ImageView img){
        imageView = img;
    }
    public void startSleep(){
        imageView.setImageResource(R.drawable.ani_sleep);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.setVisibility(View.VISIBLE);
        animationDrawable.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(3000);
                    if(animationDrawable.isRunning()){
                        animationDrawable.stop();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
