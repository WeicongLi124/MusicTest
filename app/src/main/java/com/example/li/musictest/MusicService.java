package com.example.li.musictest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;

public class MusicService extends Service {
    private static MediaPlayer mediaPlayer; //声明操作媒体的对象
    static int pos = 0; //记录播放的位置
    private static NotificationManager notificationManager;
    private String TAG = "media";
    private static Notification notification;

    class MyBinder extends Binder{
        public MusicService getService(){
            play();
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showMusicNotification();
        //初始化播放对象
        if (mediaPlayer == null){
            mediaPlayer = mediaPlayer.create(MusicService.this,R.raw.lit);
            mediaPlayer.setLooping(false);
        }
        //监听播放结束，释放资源
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }

    //用于开始播放的方法
    public static void play(){
        if (mediaPlayer != null && !mediaPlayer.isPlaying()){
            try {
                if (pos != 0){
                    //根据指定位置进行播放
                    mediaPlayer.seekTo(pos);
                    mediaPlayer.start();
                }else {
                    //从头播放
                    notificationManager.notify(200,notification);
                    mediaPlayer.stop();
                    mediaPlayer.prepare();//启动之前先prepare
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //暂停播放
    public static void pause(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            //获取播放位置
            pos = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    //停止播放
    public static void stop(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            pos = 0;
        }
    }


    public void showMusicNotification(){
        Notification.Builder builder = new Notification.Builder(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
        remoteViews.setImageViewResource(R.id.image,R.mipmap.ic_launcher);
        builder.setContent(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        //通知栏控制器播放按钮
        Intent intentPlay = new Intent("play");//新建意图，设置action标记为“play”，用于接收广播时过滤意图信息
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,0,intentPlay,0);
        remoteViews.setOnClickPendingIntent(R.id.play,playPendingIntent);//为play控件注册事件
        //通知栏控制器暂停按钮
        Intent intentPause = new Intent("pause");
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this,0,intentPause,0);
        remoteViews.setOnClickPendingIntent(R.id.pause,pausePendingIntent);
        //通知栏控制器停止按钮
        Intent intentStop = new Intent("stop");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this,0,intentStop,0);
        remoteViews.setOnClickPendingIntent(R.id.stop,stopPendingIntent);
        ////通知栏控制器关闭通知按钮
        Intent intentDead = new Intent("dead");
        PendingIntent deadPendingIntent = PendingIntent.getBroadcast(this,0,intentDead,0);
        remoteViews.setOnClickPendingIntent(R.id.dead,deadPendingIntent);

        notification = builder.build();
        notification.flags = notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public static void clearNotification(){
        notificationManager.cancel(200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
