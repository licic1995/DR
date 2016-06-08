package com.example.drpet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drpet.DataProcessPackage.*;
import com.example.drpet.MyWidget.*;
import com.example.drpet.UserOperatePackage.*;


public class MainActivity extends AppCompatActivity {


    private TextView txt_petname;
    private TextView txt_petstatus;
    private ImageButton ib_petstatus;
    private ProgressBar_PetInfo pb_happy;
    private ProgressBar_PetInfo pb_hunger;
    private ProgressBar_PetInfo pb_health;
    private ImageButton hint_happy;
    private ImageButton hint_health;
    private ImageButton hint_hunger;
    private String petname = null;


    /***
     * 初始化
     */
    private void initView(){
        txt_petname = (TextView) findViewById(R.id.txt_petname);
        txt_petstatus = (TextView) findViewById(R.id.txt_petstatus);
        ib_petstatus = (ImageButton) findViewById(R.id.ib_petstatus);
        pb_happy = (ProgressBar_PetInfo) findViewById(R.id.pb_happy);
        pb_health = (ProgressBar_PetInfo) findViewById(R.id.pb_health);
        pb_hunger = (ProgressBar_PetInfo) findViewById(R.id.pb_hunger);
        hint_happy = (ImageButton) findViewById(R.id.hint_happy);
        hint_health = (ImageButton) findViewById(R.id.hint_health);
        hint_hunger = (ImageButton) findViewById(R.id.hint_hunger);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        final FileHelper f_petname = new FileHelper("petname");


        if((petname=f_petname.f_read(this))==null){
            //第一次使用初始化名字
            final EditText et = new EditText(this);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("欢迎使用Dr Pet")
                    .setMessage("请给宠物起一个名字,不能为空，不能超过12个字符：")
                    .setIcon(R.drawable.icon_launcher)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name  = et.getText().toString();
                            if(name.equals("") || name.length()>12){
                                Toast.makeText(getApplicationContext(), "名字不符合要求！", Toast.LENGTH_SHORT).show();
                            }else{
                                f_petname.f_write(getApplicationContext(), name);
                                Toast.makeText(getApplicationContext(), "起名成功！", Toast.LENGTH_SHORT).show();
                                txt_petname.setText(name);
                            }
                        }
                    })
                    .show();
        }else{
            txt_petname.setText(petname);
        }



        if(MyPetService.isStart){
            checkIBStatus(2);
        }else{
            checkIBStatus(1);
        }
        ib_petstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyPetService.isStart){
                    Log.i("Service","<----CLOSE---->");
                    Intent intent = new Intent(MainActivity.this, MyPetService.class);
                    stopService(intent);
                    checkIBStatus(1);
                }else{
                    Log.i("Service","<----OPEN---->");
                    Intent intent = new Intent(MainActivity.this, MyPetService.class);
                    startService(intent);
                    checkIBStatus(2);
                }
                invalidateOptionsMenu();
            }
        });
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

    /***
     * 用于更新的UI的handler
     */
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ib_petstatus.setBackgroundResource(R.drawable.icon_unstart);
                txt_petstatus.setText("未开启\n←点我开启！");
            }else if(msg.what == 2){
                ib_petstatus.setBackgroundResource(R.drawable.icon_normal);
                txt_petstatus.setText("已开启\n←点我关闭 TAT");
            }
        }
    };

    /***
     * 改变ImageButtom及说明UI
     * @param icode 1服务关闭 2服务开启
     */
    private void checkIBStatus(int icode){
        if(icode == 1){
            //服务未开启
            Log.i("MSG","sending...");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
            thread.start();
        }else{
            //服务已开启
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            });
            thread.start();
        }
    }
}
