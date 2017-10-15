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
        switch (v.getId()){
            case R.id.play:
                if (musicService == null) {
                    intent = new Intent(MainActivity.this,MusicService.class);
                    bindService(intent,myConnection, Context.BIND_AUTO_CREATE);
                    startService(intent);
                }else {
                    musicService.play();
                }
                break;
            case R.id.pause:
                if (musicService != null){
                    musicService.pause();
                }
                break;
            case R.id.stop:
                if (musicService != null){
                    musicService.stop();
                }
                break;
        }
    }



    class MusicConnection implements ServiceConnection {

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
