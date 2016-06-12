package com.example.drpet;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.*;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import com.example.drpet.AnimationPackage.AnimationPlayer;
import com.example.drpet.DataProcessPackage.FileHelper;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        AnimationPlayer animationPlayer = new AnimationPlayer((ImageView) findViewById(R.id.iv_alarm));
        Log.i("alarm","ringgggggggggg------");
        Button btn ;
        btn = (Button) findViewById(R.id.btn_closealarm);
        final MediaPlayer player = new MediaPlayer();
        Uri uri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try{
            player.setDataSource(this,uri);
            Log.i("MEDIA","set source");
        }catch (IOException e) {
            e.printStackTrace();
        }
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getStreamVolume(AudioManager.STREAM_ALARM)!=0){
            Log.i("MEDIA","setstreamVolume");
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.prepare();
                Log.i("MEDIA","prepare");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("MEDIA","start");
            player.start();
        }
        animationPlayer.startRing();
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.stop();
                    FileHelper f_alarmtime = new FileHelper("alarmtime");
                    f_alarmtime.f_write(getApplicationContext(), "", Context.MODE_PRIVATE);
                    finish();
                }
            });
        }
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                AnimationPlayer animationPlayer = new AnimationPlayer((ImageView) findViewById(R.id.iv_desktop));
//                if((ImageView) findViewById(R.id.iv_desktop) == null){
//                    Log.i("alarm","ERROR");
//                }else{
//                    animationPlayer.startSleep();
//                }
//            }
//        });
//        finish();
    }
}
