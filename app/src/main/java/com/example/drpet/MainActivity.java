package com.example.drpet;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.drpet.DataProcessPackage.*;
import com.example.drpet.MyWidget.*;
import com.example.drpet.UserOperatePackage.*;

public class MainActivity extends AppCompatActivity {


    private TextView txt_petname;
    private ProgressBar_PetInfo pb_happy;
    private ProgressBar_PetInfo pb_hunger;
    private ProgressBar_PetInfo pb_health;
    private ImageButton hint_happy;
    private ImageButton hint_health;
    private ImageButton hint_hunger;
    private String petname = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_petname = (TextView) findViewById(R.id.txt_petname);
        pb_happy = (ProgressBar_PetInfo) findViewById(R.id.pb_happy);
        pb_health = (ProgressBar_PetInfo) findViewById(R.id.pb_health);
        pb_hunger = (ProgressBar_PetInfo) findViewById(R.id.pb_hunger);
        hint_happy = (ImageButton) findViewById(R.id.hint_happy);
        hint_health = (ImageButton) findViewById(R.id.hint_health);
        hint_hunger = (ImageButton) findViewById(R.id.hint_hunger);
        FileHelper f_petname = new FileHelper("petname");
        if((petname=f_petname.f_read(this))==null){
            //TODO: 第一次使用初始化名字
        }else{
            txt_petname.setText(petname);
        }
        if(!MyPetService.isStart){
            //TODO:服务未开启
        }else{
            //TODO:服务已开启
        }

        hint_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示——快乐")
                        .setMessage("和宠物互动可以让宠物更快乐哦！")
                        .setPositiveButton("明白了！",null)
                        .show();
            }
        });
        hint_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示——健康")
                        .setMessage("早点睡觉少玩手机的话，可以让宠物更健康哦！")
                        .setPositiveButton("明白了！",null)
                        .show();
            }
        });
        hint_hunger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示——饥饿")
                        .setMessage("喂食可以恢复饥饿哦！\nTIPS：在每天的饭点才能喂食哦！")
                        .setPositiveButton("明白了！", null)
                        .show();
            }
        });
    }
}
