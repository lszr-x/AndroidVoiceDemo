package com.example.lszr.androidvoicedemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * @author：lszr on 2018/3/28 15:23
 * @email：1085963811@qq.com
 */
public class VolumeControlClass extends AppCompatActivity {
    public void changeVolume(){
        //声明AudioManager对象并初始化
        AudioManager audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

//        控制闹钟音量
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_LOWER,0);
//        控制DTMF音调的声音
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF,0,0);
        //媒体声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        //系统提示音
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
        //电话铃声
        audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
        //系统音量
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
        //通话语音音量
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,100,AudioManager.FLAG_ALLOW_RINGER_MODES);
    }


    public void playAMusic() throws IOException {

    }



}
