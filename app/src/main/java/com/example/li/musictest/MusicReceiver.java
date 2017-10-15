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
            MusicService.play();
        }else if ("pause".equals(ctrl_code)){
            MusicService.pause();
        }else if ("stop".equals(ctrl_code)){
            MusicService.stop();
        }else if ("dead".equals(ctrl_code)){
            MusicService.clearNotification();
            MusicService.stop();
        }
    }
}
