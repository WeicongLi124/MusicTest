package com.example.li.musictest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author: Frank
 * @time: 2017/10/14 19:01
 * 实现的主要功能:主activity
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    private MusicService musicService;
    private MusicConnection myConnection = new MusicConnection();
    private String TAG = "media";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //activity页面的相关点击操作
        switch (v.getId()){
            //播放
            case R.id.play:
                if (musicService == null) {
                    //首次播放绑定服务
                    intent = new Intent(MainActivity.this,MusicService.class);
                    bindService(intent,myConnection, Context.BIND_AUTO_CREATE);
                    startService(intent);
                }else {
                    musicService.play();
                }
                break;
            //暂停
            case R.id.pause:
                if (musicService != null){
                    musicService.pause();
                }
                break;
            //停止
            case R.id.stop:
                if (musicService != null){
                    musicService.stop();
                }
                break;
        }
    }



    private class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            musicService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myConnection = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myConnection);
        MusicService.clearNotification();
        Log.i(TAG, "onDestroy: ");
    }
}
