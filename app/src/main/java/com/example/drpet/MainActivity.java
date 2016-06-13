package com.example.drpet;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.drpet.AlarmManager.AlarmReceiver;
import com.example.drpet.DataProcessPackage.*;
import com.example.drpet.MyWidget.*;
import com.example.drpet.UserOperatePackage.*;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    private TextView txt_petname;
    private TextView txt_petstatus;
    private TextView txt_alarmstatus;
    private ImageButton ib_petstatus;
    private ProgressBar_PetInfo pb_happy;
    private ProgressBar_PetInfo pb_hunger;
    private ProgressBar_PetInfo pb_health;
    private ImageButton hint_happy;
    private ImageButton hint_health;
    private ImageButton hint_hunger;
    private Button btn_alarm;
    private Button btn_alarmcancel;
    private PendingIntent  intent_alarm;
    private String petname = null;
    public  FileHelper f_alarmtime;
    /***
     * 闹钟
     */
    private AlarmManager alarmManager=null;
    Calendar cal= Calendar.getInstance();

    /***
     * 初始化
     */
    private void initView(){
        txt_petname = (TextView) findViewById(R.id.txt_petname);
        txt_petstatus = (TextView) findViewById(R.id.txt_petstatus);
        txt_alarmstatus = (TextView) findViewById(R.id.tv_alarmStatus);
        ib_petstatus = (ImageButton) findViewById(R.id.ib_petstatus);
        pb_happy = (ProgressBar_PetInfo) findViewById(R.id.pb_happy);
        pb_health = (ProgressBar_PetInfo) findViewById(R.id.pb_health);
        pb_hunger = (ProgressBar_PetInfo) findViewById(R.id.pb_hunger);
        hint_happy = (ImageButton) findViewById(R.id.hint_happy);
        hint_health = (ImageButton) findViewById(R.id.hint_health);
        hint_hunger = (ImageButton) findViewById(R.id.hint_hunger);
        btn_alarm = (Button) findViewById(R.id.btn_alarmSwitch);
        btn_alarmcancel = (Button) findViewById(R.id.btn_alarmCancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }

        initView();


        final FileHelper f_petname = new FileHelper("petname");
        f_alarmtime = new FileHelper("alarmtime");
        Message msg = new Message();
        msg.what = 4;
        handler.sendMessage(msg);

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
                                f_petname.f_write(getApplicationContext(), name, Context.MODE_APPEND);
                                Toast.makeText(getApplicationContext(), "起名成功！", Toast.LENGTH_SHORT).show();
                                txt_petname.setText(name);
                            }
                        }
                    })
                    .show();
        }else{
            txt_petname.setText(petname);

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //更新宠物信息条
                if(PetInfo.petInfoManager == null)
                    PetInfo.initManager(getApplicationContext(), petname);
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                Log.i("sendmsg","sendmsg");
                //----------------------------
            }
        }).start();


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
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker timePicker, final int hourOfDay, final int minute) {
                                Calendar c=Calendar.getInstance();//获取日期对象
                                c.setTimeInMillis(System.currentTimeMillis());        //设置Calendar对象
                                c.set(Calendar.HOUR, hourOfDay);        //设置闹钟小时数
                                c.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                                c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                                c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);    //创建Intent对象
                                intent_alarm = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);    //创建PendingIntent
                                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, c.getTimeInMillis(), intent_alarm);        //设置闹钟
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intent_alarm );        //设置闹钟，当前时间就唤醒
                                Toast.makeText(MainActivity.this, "闹钟设置成功", Toast.LENGTH_LONG).show();//提示用户
                                Log.i("ALARM",String.valueOf(c.getTimeInMillis())+"  "+String.valueOf(System.currentTimeMillis()));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                       f_alarmtime.f_write(getApplicationContext(),"   /"+String.valueOf(hourOfDay)+":" +
                                               String.valueOf(minute)+"/",Context.MODE_APPEND);
                                        Message msg = new Message();
                                        msg.what = 4;
                                        handler.sendMessage(msg);
                                    }
                                }).start();
                            }
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false).show();

            }
        });
        btn_alarmcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(intent_alarm);
                Toast.makeText(MainActivity.this, "闹钟取消成功", Toast.LENGTH_LONG).show();//提示用户
                f_alarmtime.f_write(getApplicationContext(), "",Context.MODE_PRIVATE);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Message msg =new Message();
        msg.what = 3;
        handler.sendMessage(msg);
    }

    /***
     * 用于更新的UI的handler
     * msg.what 1 设置ImageButton 未开启状态时
     * msg.what 2 设置ImageButton 开启状态时
     * msg.what 3 设置宠物信息条内容
     */
    final public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ib_petstatus.setBackgroundResource(R.drawable.icon_unstart);
                txt_petstatus.setText("未开启\n←点我开启！");
            }else if(msg.what == 2){
                ib_petstatus.setBackgroundResource(R.drawable.icon_normal);
                txt_petstatus.setText("已开启\n←点我关闭 TAT");
            }else if(msg.what == 3){
                Log.i("handler","up jindutiao"+String.valueOf(PetInfo.getHappy()));
                pb_happy.setProgress(PetInfo.getHappy());
                pb_hunger.setProgress(PetInfo.getHunger());
                pb_health.setProgress(PetInfo.getHealth());
            }else if(msg.what == 4){
                String alarmtime = f_alarmtime.f_read(getApplicationContext());
                txt_alarmstatus.setText(alarmtime);
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
