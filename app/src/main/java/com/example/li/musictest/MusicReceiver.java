package com.example.li.musictest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author: Frank
 * @time: 2017/10/14 19:01
 * 实现的主要功能:创建广播接收服务发送的消息
 */

public class MusicReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String ctrl_code = intent.getAction();
        if ("play".equals(ctrl_code)){
            //通知栏音乐播放操作
            MusicService.play();
        }else if ("pause".equals(ctrl_code)){
            //通知栏音乐暂停操作
            MusicService.pause();
        }else if ("stop".equals(ctrl_code)){
            //通知栏音乐停止操作
            MusicService.stop();
        }else if ("clear".equals(ctrl_code)){
            //通知栏关闭通知操作
            MusicService.clearNotification();
            MusicService.stop();
        }
    }
}
