package com.example.drpet.AnimationPackage;

import android.app.Activity;
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
    public void moveeOver(){
        imageView.setImageResource(R.drawable.ani_moveover);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.setVisibility(View.VISIBLE);
        animationDrawable.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(1500);
                    if(animationDrawable.isRunning()){
                        animationDrawable.stop();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void startRing(){
        imageView.setImageResource(R.drawable.ani_ring);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }
    public void startEat(){
        imageView.setImageResource(R.drawable.ani_eat);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.setVisibility(View.VISIBLE);
        animationDrawable.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);
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
